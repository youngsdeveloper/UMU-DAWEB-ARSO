import { Card } from "react-bootstrap";
import ReactStars from "react-rating-stars-component";

export default function Valoracion({email,calificacion,comentario}){

    return (
        <Card>
            <Card.Body>
                <Card.Text>

                    <ReactStars
                            count={5}
                            size={50}
                            value={calificacion}
                            isHalf={true}
                            edit={false}
                        />
                    <p>
                        {comentario}
                    </p>
                </Card.Text>
            </Card.Body>
        </Card>
    )
}