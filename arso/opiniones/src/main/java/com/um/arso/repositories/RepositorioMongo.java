package com.um.arso.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.um.arso.repositories.especificacion.Specification;


public abstract class RepositorioMongo<T extends Identificable> implements RepositorioString<T> {

	private MongoCollection<T> collection;

	public RepositorioMongo() {

		ConnectionString connectionString = new ConnectionString(getMongoURL());
		
		//Obtener un codec por defecto
		CodecRegistry defacultCodecRegistry = CodecRegistries.fromRegistries
			(MongoClientSettings.getDefaultCodecRegistry(),CodecRegistries.fromProviders(PojoCodecProvider.
			builder().automatic(true).build()));

		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.codecRegistry(defacultCodecRegistry)
				.serverApi(ServerApi.builder()
					.version(ServerApiVersion.V1)
					.build())
				.build();

		MongoClient mongoClient = MongoClients.create(settings);
		
		
		MongoDatabase mongoDatabase = mongoClient.getDatabase(getDatabaseName());

		String collection_name = getCollectionName();

		this.collection = (MongoCollection<T>) mongoDatabase.getCollection(collection_name, getType()).withCodecRegistry(defacultCodecRegistry);
		
		//DONE: Desacoplar repositorio de una clase en concreto
	}

	public abstract Class<T> getType();

	public abstract String getDatabaseName();
	public abstract String getMongoURL();
	public abstract String getCollectionName();

	

	@Override
	public String add(T entity) throws RepositorioException {


		InsertOneResult result = collection.insertOne(entity);
		if (result.getInsertedId() != null){
			String id = result.getInsertedId().asObjectId().getValue().toString();
			entity.setId(id);
			return id;
		}
		return null;
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		T element = collection.findOneAndReplace(Filters.eq("_id", new ObjectId(entity.getId())), entity);
		if(element==null){
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		}
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		DeleteResult deleteResult = collection.deleteOne(Filters.eq("_id", new ObjectId(entity.getId())));
		if(deleteResult.getDeletedCount()<1){
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		}
	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		T element = collection.find(Filters.eq("_id", new ObjectId(id))).first();
		if(element==null){
			throw new EntidadNoEncontrada(id + " no existe en el repositorio");
		}
		return element;
	}

	@Override
	public List<T> getAll() throws RepositorioException {

		MongoCursor<T> it = collection.find().iterator();
		List<T> entidades = new ArrayList<T>();
		while (it.hasNext()) {
			entidades.add(it.next());
		}
		return entidades;		
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		MongoCursor<T> it = collection.find().projection(Projections.fields(Projections.include("_id"))).iterator();
		List<String> entidades = new ArrayList<String>();
		while (it.hasNext()) {
			entidades.add(it.next().getId());
		}
		return entidades;
	}

	@Override
	public List<T> search(Specification<T> specification){

		MongoCursor<T> it = collection.find(specification.toFilter()).sort(specification.toComparator()).iterator();

		List<T> entidades = new ArrayList<T>();
		while (it.hasNext()) {
			entidades.add(it.next());
		}


		return entidades;
	}

}