import { Card } from "react-bootstrap";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCity,faEye } from '@fortawesome/free-solid-svg-icons';


import './Restaurante.css'

export default function Restaurante({id,nombre,ciudad}){

    const urlRestaurante = `http://localhost:3000/restaurantes/${id}`;

    return (
        <Card className="restaurante mb-3">
            <Card.Body className="text-align-center">
                <Card.Title>
                    {nombre}
                </Card.Title>
                <Card.Text>
                    <FontAwesomeIcon icon={faCity}/>
                    <span className="ciudad">
                        {ciudad}
                    </span>
                </Card.Text>
                <div className="buttons">
                    <a className="btn btn-primary"href={urlRestaurante}> Realizar pedido</a>
                    <a className="btn btn-secondary"href={urlRestaurante}> <FontAwesomeIcon icon={faEye}/><span>Ver</span></a>

                </div>
            </Card.Body>
        </Card>
    )
}