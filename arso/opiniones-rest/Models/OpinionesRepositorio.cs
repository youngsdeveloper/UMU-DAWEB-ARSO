using Repositorio;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using Opiniones.Modelo;

namespace Opniniones.Repositorio
{


    public class RepositorioOpinionesMongoDB : Repositorio<Opinion, string>
    {
        private readonly IMongoCollection<Opinion> opiniones;

        public RepositorioOpinionesMongoDB()
        {
            //DONE: Cambiar Parámetros

            var mongoURL = Environment.GetEnvironmentVariable("MONGO_URL");


            var client = new MongoClient(mongoURL);
            var database = client.GetDatabase("arso");

            opiniones = database.GetCollection<Opinion>("opinion");
        }

        public string Add(Opinion entity)
        {
            opiniones.InsertOne(entity);

            return entity.id;
        }

        public void Update(Opinion entity)
        {
            opiniones.ReplaceOne(opinion => opinion.id == entity.id, entity);
        }

        public void Delete(Opinion entity)
        {
            opiniones.DeleteOne(opinion => opinion.id == entity.id);
        }

        public List<Opinion> GetAll()
        {
            return opiniones.Find(_ => true).ToList();
        }

        public Opinion GetById(string id)
        {
            return opiniones
                .Find(opinion => opinion.id == id)
                .FirstOrDefault();
        }

        public List<string> GetIds()
        {
            var todas =  opiniones.Find(_ => true).ToList();

            return todas.Select(p => p.id).ToList();

        }

        public Opinion GetByRecurso(string recurso)
        {
            return opiniones
                .Find(opinion => opinion.recurso == recurso)
                .FirstOrDefault();
        }
    }
}