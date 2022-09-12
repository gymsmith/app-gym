package com.todoteg.controller;

import java.net.URI;

/*import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;*/
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todoteg.model.HttpushResponseUtil;
import com.todoteg.model.HuellaTemp;

import com.todoteg.service.IHuellaTempService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/huella_temp") // se establece la direccion url particular
public class HuellaTempController {

	@Autowired
	private IHuellaTempService service;
	
	//ResponseEntity: Clase que me permite extender funcionalidades sobre la peticion http, controlar el status code.

	
	@PostMapping("/httpush")
	public Mono<ResponseEntity<HttpushResponseUtil>> httpush(@RequestParam("timestamp") String timestamp, @RequestParam("token") String token) throws InterruptedException{
		Thread t = new Thread( ()-> {
		long fecha_actual = 0;
		long fecha_bd = 0;
		
		System.out.println(timestamp);
		
		fecha_actual = (timestamp.equals("null")) ? 0: Long.parseLong(timestamp);
		System.out.println(fecha_actual);

		long elapsedTime = 0;
		int i = 0;
		while (fecha_bd <= fecha_actual) {
			List<HuellaTemp> updateTime =  service.ObtenerUpdateTimePorSerial(token, "update_time").block();

		   if (updateTime.size() > 0) {
			   
			   if(updateTime.get(0).getStatusPlantilla()=="Muestras Restantes: 0") {
				   break;
			   }
			   else if(updateTime.get(0).getUpdate_time() != null) {				   
				   fecha_bd = updateTime.get(0).getUpdate_time().getTime() / 1000;
			   }
			   var fechabd = (updateTime.get(0).getUpdate_time() == null)? 0:updateTime.get(0).getUpdate_time().getTime() / 1000;
			   System.out.println("/httpush -> vuelta"+i+" = "+updateTime.get(0).getUpdate_time()+" - fecha bd = "+ fechabd + " - fecha actual = " + fecha_actual);
		   }
		   
		   elapsedTime = elapsedTime + 1;
		    if (elapsedTime == 600) {//modificar aqui si se requiere reiniciar em menos tiempo
		        break;
		    }
		   i++;
		}
		System.out.println(fecha_actual);
		System.out.println(fecha_bd);
		
		
		
		
		});
		
		t.start(); // Inicia el Hilo
		t.join();  // espera que el hilo anterior termine su ejecucion para continuar con el principal
		
		return service.ObtenerHuellaHttpush()
				.map(httpush -> ResponseEntity
					.ok() // indica el status code de respuesta
					.contentType(MediaType.APPLICATION_JSON)
					.body(httpush)
				);
		
		
	}
	
	@PostMapping("/ActivarSensor")
	public Mono<ResponseEntity<HuellaTemp>> ActivarSensorAdd(@Valid @RequestParam("token") String token,@RequestParam("opc") String opc, final ServerHttpRequest req){

		HuellaTemp nuevaHuella = new HuellaTemp();
		nuevaHuella.setPc_serial(token);
		
		switch (opc) {
        case "lectura":
        	nuevaHuella.setTexto("El sensor de huella dactilar esta activado");
			nuevaHuella.setOpc("leer");
            break;
        case "capturar":
        	nuevaHuella.setTexto("El sensor de huella dactilar esta activado");
			nuevaHuella.setStatusPlantilla("Muestras Restantes: 4");
			nuevaHuella.setOpc("capturar");
            break;
		}
		 
		 Mono<HuellaTemp> huella = Mono.just(nuevaHuella);
		 
		 return huella
				 .flatMap(H -> {
					 return service.ObtenerHuellaTempPorSerial(token)
							 .map(hBD -> {
								 H.setId(hBD.getId());
								 return H;
							 })
							 .defaultIfEmpty(H);
				 })
				 .flatMap(H -> {
					 if (H.getId() != null) {
						 return service.eliminar(H.getId())
								 .thenReturn(H);
					 }
					 return Mono.just(H);
				 })
				 .flatMap(service::registrar)
				 .map(newHuella -> ResponseEntity
									.created(URI.create(req.getURI().toString().concat("/").concat(newHuella.getId()))) // se obtiene url dinamica del recurso
									.contentType(MediaType.APPLICATION_JSON)
									.body(newHuella));
	}
	
	
	// @Valid Para hacer cumplir las validaciones propuestas en el modelo
	
	
	@DeleteMapping
	public Mono<ResponseEntity<Void>> eliminar(){
		return service.eliminarTodo()
				.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}

