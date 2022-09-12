package com.todoteg.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
//import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoteg.model.Cliente;
import com.todoteg.model.ClienteInfoUtil;
import com.todoteg.model.Plan;
import com.todoteg.model.Seguimiento;
import com.todoteg.pagination.PageSupport;
import com.todoteg.repo.IClienteRepo;
import com.todoteg.repo.IGenericRepo;
import com.todoteg.repo.IHuellaTempRepo;
import com.todoteg.repo.IPlanRepo;
import com.todoteg.repo.ISeguimientoRepo;
import com.todoteg.service.IClienteService;

import reactor.core.publisher.Mono;


@Service
public class ClienteServiceImpl extends CRUDImpl<Cliente, String> implements IClienteService {
	
	@Autowired
	private IClienteRepo repo;
	
	@Autowired
	private IPlanRepo repoPlan;
	
	@Autowired
	private ISeguimientoRepo repoSegui;
	
	@Autowired
	private IHuellaTempRepo repoHuella;
	/*
	 * @Autowired private BCryptPasswordEncoder bcrypt;
	 */

	@Override
	protected IGenericRepo<Cliente, String> getRepo() {
		return repo;
	}
	
	@Override
	public Mono<Cliente> registrar(String serial, Cliente cliente){
		return repoHuella.findByPcSerial(serial)
				.flatMap(Huella -> {
					cliente.setHuella(Huella.getHuella());
					cliente.setImgHuella(Huella.getImgHuella());
					return repoHuella.deleteById(Huella.getId())
							.thenReturn(cliente);
					
				})
				.flatMap(repo::save);
		
	}
	
	@Override
	public Mono<Cliente> modificar(Cliente cliente){
		return repo.save(cliente)
				.flatMap(c -> {
					if(c.getSubscripcion() != null) {
						return repoPlan.findById(c.getSubscripcion().getPlan().getId())
								.map(P -> {
									Plan plan = new Plan();
									plan.setId(P.getId());
									plan.setTitulo(P.getTitulo());
									c.getSubscripcion().setPlan(plan);
									return c;
									})
								.defaultIfEmpty(c);
					}
					return Mono.just(c);
						
				});
	}

	/*
	 * @Override public Mono<User> ObtenerUsuarioPorNombre(String usuario) {
	 * Mono<Cliente> ClienteEncontrado = repo.findById(usuario); List<String> roles
	 * = new ArrayList<String>(); roles.add("USER");
	 * 
	 * return ClienteEncontrado .flatMap(u -> {
	 * u.setIdentificacion(bcrypt.encode(u.getIdentificacion())); // se cumple con
	 * el requerimiento del metodo; y se crea una instancia de user requerido por
	 * spring security return Mono.just(new User(u.getNombres(),
	 * u.getIdentificacion(), u.getActivo(), roles)); }); }
	 */

	@Override
	public Mono<ClienteInfoUtil> buscarPorId(String id) {
		PageRequest rango = PageRequest.of(0, 5, Sort.Direction.DESC, "fecha");
		return repo.findById(id)
				.flatMap(cliente -> {
					if(cliente.getSubscripcion() != null) {
						return repoPlan.findById(cliente.getSubscripcion().getPlan().getId())
							.map(P -> {
								Plan plan = new Plan();
								plan.setId(P.getId());
								plan.setTitulo(P.getTitulo());
								cliente.getSubscripcion().setPlan(plan);
								return cliente;
							})
							.defaultIfEmpty(cliente);
					}
					return Mono.just(cliente);
				})
				.flatMap(cliente -> {
					ClienteInfoUtil clienteInfo = new ClienteInfoUtil();
					HashMap<String,String> clienteID = new HashMap<String, String>();
					clienteID.put("_id", cliente.getIdentificacion());
					
					clienteInfo.setIdentificacion(cliente.getIdentificacion());
					clienteInfo.setNombres(cliente.getNombres());
					clienteInfo.setTelefono(cliente.getTelefono());
					//clienteInfo.setEdad(cliente.getEdad());
					clienteInfo.setPeso(cliente.getPeso());
					clienteInfo.setAltura(cliente.getAltura());
					clienteInfo.setSexo(cliente.getSexo());
					clienteInfo.setActivo(cliente.getActivo());
					clienteInfo.setSubscripcion(cliente.getSubscripcion());
					
					return repoSegui.findByUsuario(clienteID, rango)
							.map(s -> {
								Seguimiento seguimiento = new Seguimiento();
								seguimiento.setPeso(s.getPeso());
								seguimiento.setMedidaCuello(s.getMedidaCuello());
								seguimiento.setMedidaPecho(s.getMedidaPecho());
								seguimiento.setMedidaAbdomen(s.getMedidaAbdomen());
								seguimiento.setMedidaBiceps(s.getMedidaBiceps());
								seguimiento.setMedidaMuslo(s.getMedidaMuslo());
								seguimiento.setMedidaPantorrilla(s.getMedidaPantorrilla());
								seguimiento.setMedidaHombro(s.getMedidaHombro());
								seguimiento.setMedidaCintura(s.getMedidaCintura());
								seguimiento.setMedidaCadera(s.getMedidaCadera());
								seguimiento.setFecha(s.getFecha());
								return seguimiento;
							})
							.collectList()
							.flatMap(Lista -> {
								clienteInfo.setSeguimiento(Lista);
								return Mono.just(clienteInfo);
							});
					
				});
	}

	@Override
	public Mono<PageSupport<ClienteInfoUtil>> ListarPagina(String busquedaPorNombre, Pageable pagina) {
		PageRequest rango = PageRequest.of(0, 5, Sort.Direction.DESC, "fecha");
		
		return repo.allByNombresAndPlan(busquedaPorNombre)
				.flatMap((totalClientes)->{
					return repo.buscarUsuarios(busquedaPorNombre, pagina) // flux<Usuarios>
							.flatMap(u -> {
								if(u.getSubscripcion() != null) {
									return repoPlan.findById(u.getSubscripcion().getPlan().getId())
											.map(P -> {
												Plan plan = new Plan();
												plan.setId(P.getId());
												plan.setTitulo(P.getTitulo());
												u.getSubscripcion().setPlan(plan);
												return u;
												})
											.defaultIfEmpty(u);
								}
								return Mono.just(u);
									
							})
							.flatMap(cliente -> {
								ClienteInfoUtil clienteInfo = new ClienteInfoUtil();
								HashMap<String,String> clienteID = new HashMap<String, String>();
								clienteID.put("_id", cliente.getIdentificacion());
								
								// Calcular Edad
								LocalDate fechaNacimiento = cliente.getFechaNacimiento();
								LocalDate fechaActual = LocalDate.now();
								int edad = fechaActual.getYear() - fechaNacimiento.getYear();
								int diferenciaMeses = fechaActual.getMonthValue() - fechaNacimiento.getMonthValue();
								if(diferenciaMeses <= 0 && fechaActual.getDayOfMonth() < fechaNacimiento.getDayOfMonth()) {
									edad--;
								}
								
								clienteInfo.setIdentificacion(cliente.getIdentificacion());
								clienteInfo.setNombres(cliente.getNombres());
								clienteInfo.setTelefono(cliente.getTelefono());
								clienteInfo.setEdad(edad);
								clienteInfo.setAltura(cliente.getAltura());
								clienteInfo.setSexo(cliente.getSexo());
								clienteInfo.setImgHuella(cliente.getImgHuella());
								clienteInfo.setFirma(cliente.getFirma());
								clienteInfo.setActivo(cliente.getActivo());
								clienteInfo.setSubscripcion(cliente.getSubscripcion());
								
								return repoSegui.findByUsuario(clienteID, rango)
										.map(s -> {
											Seguimiento seguimiento = new Seguimiento();
											seguimiento.setId(s.getId());
											seguimiento.setPeso(s.getPeso());
											seguimiento.setMedidaCuello(s.getMedidaCuello());
											seguimiento.setMedidaPecho(s.getMedidaPecho());
											seguimiento.setMedidaAbdomen(s.getMedidaAbdomen());
											seguimiento.setMedidaBiceps(s.getMedidaBiceps());
											seguimiento.setMedidaMuslo(s.getMedidaMuslo());
											seguimiento.setMedidaPantorrilla(s.getMedidaPantorrilla());
											seguimiento.setMedidaHombro(s.getMedidaHombro());
											seguimiento.setMedidaCintura(s.getMedidaCintura());
											seguimiento.setMedidaCadera(s.getMedidaCadera());
											seguimiento.setFecha(s.getFecha());
											return seguimiento;
										})
										.collectList()
										.flatMap(Lista -> {
											clienteInfo.setSeguimiento(Lista);
											return Mono.just(clienteInfo);
										});
								
							})
							.collectList()
							.map(ClienteLista -> new PageSupport<>(
									ClienteLista,
									pagina.getPageNumber(),pagina.getPageSize(), totalClientes
									));
				});
		
	}
	

}
