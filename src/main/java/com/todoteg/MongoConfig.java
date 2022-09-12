package com.todoteg;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

// Clase de configuracion necesaria para la anulacion del atributo _class que se agrega por defecto en los documentos
@Configuration
public class MongoConfig implements InitializingBean {
	@Autowired
	@Lazy // Solo se va a generar el bean cuando sea necesario en el caso por defecto se va generar apenas se levante el aplicativo
	private MappingMongoConverter mappingMongoConverter; // Esto es una instancia propia de spring

	@Override
	public void afterPropertiesSet() throws Exception {
		// setTypeMaper hace referencia al _class en el documento mongo
		// y este se establce como nulo para evitar que se agregue al BSON
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
	}
}
