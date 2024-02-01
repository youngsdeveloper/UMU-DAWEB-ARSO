import { useEffect,useState } from "react";
import { useParams } from 'react-router-dom';
import { Card, Row, Col, Container, Form, Button, Alert } from "react-bootstrap";
import ReactStars from "react-rating-stars-component";
import Cookies from 'js-cookie';

import Valoracion from './Valoracion'

export default function RestauranteOpiniones(){



    const [opinion,setOpinion] = useState(null);

    const [valoraciones,setValoraciones] = useState([]);

    const [numEstrellas,setEstrellas] = useState(0)
    const [email,setEmail] = useState('');
    const [comentario,setComentario] = useState('');
    
    const { id } = useParams();

    const [successValoracion,setSuccessValoracion] = useState(false);

    useEffect(() => {

        fetch("http://localhost:3000/opiniones/"+id,{
              credentials: "include"
          })
          .then(res => {
            if(res.status!=200){
                throw new Error(res.status)
            }
            return res.json()
            })
          .then(data => {
            setOpinion(data)
            setValoraciones(data.valoraciones)
          })
          .catch(function(error){
            localStorage.removeItem("user");
            window.location.replace('http://localhost:3000/login');
          });;
  

    },[])

    const estrellasCambiadas=(event) => {
        setEstrellas(event)
    }

    const handleInputEmail=(event) => {
        setEmail(event.target.value)
    }

    const handleInputComentario=(event) => {
        setComentario(event.target.value)
    }

    const isValidMail = (mail) => {

        const validMail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

        return email!=null && email.match(validMail);

    };

    const clearForm = () => {
        setEmail('')
        setEstrellas(0)
        setComentario('')
    }

    const handleSubmit=() => {

        if(!isValidMail(email)){
            return;
        }

        fetch("http://localhost:3000/opiniones/"+id,{
          method : "POST",
          credentials: "include",
          headers :{
            "Content-Type" : 'application/json',
          },
          body : JSON.stringify({email: email,
                    calificacion: numEstrellas,
                    comentario: comentario
            })
            })
        .then(res => {
            if(res.status==200){

                setSuccessValoracion(true);
                clearForm();

                //Recargamos opiniones
                fetch("http://localhost:3000/opiniones/"+id,{
                    credentials: "include"
                })
                .then(res => res.json())
                .then(data => {
                    setOpinion(data)
                    setValoraciones(data.valoraciones)
                    
                });
            }
        })
    }

    return (
        <Container className="mt-4">
        <Row>

            <Col xs={6}>
                <Card>
                    <Card.Body>
                        <Card.Title className="mb-3">
                            Opiniones
                        </Card.Title>
                        <Card.Text>
                            {
                                valoraciones.length>0 ?
                                    valoraciones.reverse().map(val => (
                                            <div className="mb-4" key={val.email}>
                                                <Valoracion calificacion={val.calificacion} comentario={val.comentario}/>
                                            </div>
                                        ))
                                        :
                                    <Alert variant="danger">
                                        No hay valoraciones registradas
                                    </Alert>
        
                            }

                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col xs={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>
                            Envía tu opinión
                        </Card.Title>
                        <Card.Text>


                            {successValoracion ?

                            <Alert variant="success">
                                Valoración registrada correctamente
                            </Alert>

                            :''}

                            <Form >
                                <Form.Group className="mb-3" >
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control type="email" value={email} required placeholder="enrique@um.es" onChange={handleInputEmail}/>
                                </Form.Group>
                                
                                <Form.Group className="mb-3">
                                    <Form.Label>Valoración</Form.Label>
                                    <ReactStars
                                        count={5}
                                        size={50}
                                        isHalf={true}
                                        edit={true}
                                        value={numEstrellas}
                                        onChange={estrellasCambiadas}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Comentario</Form.Label>
                                    <textarea onChange={handleInputComentario} rows="6" className="form-control" value={comentario}></textarea>
                                </Form.Group>
                                <Button disabled={!isValidMail(email)} onClick={handleSubmit}>Enviar valoración</Button>

                            </Form>
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
        </Container>

    )
}