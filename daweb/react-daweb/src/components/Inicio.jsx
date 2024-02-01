import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass,faRocket,faSackDollar,faUtensils } from '@fortawesome/free-solid-svg-icons';
import Carousel from 'react-bootstrap/Carousel';
import { useState } from 'react';
import { Link,useNavigate } from 'react-router-dom';


export default function Inicio(){

    const [q,setQ] = useState('');

    const handleInputQ = (event)=>{
        setQ(event.target.value) 
    }
    
    const navigate = useNavigate();

    const navigateToRestaurantes = ()=>{
        navigate(`/restaurantes?q=${q}`);
    }

    const onInputQKeyDown = (event)=>{
        if(event.key=='Enter'){
            navigateToRestaurantes()
        }
    }

    return (
        <div>
            <div className="intro">
                <Carousel>
                    <Carousel.Item className="carousel-01">
                        <div>
                        <h1 className="h1">¡Pide comida al estilo REST!</h1>
                        </div>
                    </Carousel.Item>
                    <Carousel.Item className="carousel-02">
                        <div>
                        <h1 className="h1">Date un REST-piro</h1>
                        </div>
                    </Carousel.Item>
                </Carousel>
            </div>

            <div className="card mt-3 mb-3 searchbox">
                <h4 className="card-title pt-4">
                    Encuentra tu restaurante ideal
                </h4>
                <div className="card-body">
                    <div className="input-group mb-3">
                    <input type="text" className="form-control" onKeyDown={onInputQKeyDown} value={q} onChange={handleInputQ} placeholder="El restaurante que tanto estabas buscando..." aria-label="Recipient's username" aria-describedby="button-addon2" />
                    
                    <button onClick={navigateToRestaurantes} className="btn btn-dark btn-lg" type="button" id="button-addon2">
                        <FontAwesomeIcon icon={faMagnifyingGlass} />
                    </button>

                    
                    </div>
                </div>
            </div>
            <div className="container text-center mt-3">
                <div className="row">
                    <div className="col-lg p-3">
                    <div className="card mx-auto" style={{width: "18rem"}}>
                        <div className="card-body">
                        <div className="icon-xl">
                            <FontAwesomeIcon icon={faRocket} />
                        </div>
                        <h5 className="card-title">¡Los más rápidos!</h5>
                        <h6 className="card-subtitle mb-2 text-body-secondary">No esperes una eternidad para recibir tu comida.</h6>
                        </div>
                    </div>
                    </div>
                    <div className="col-lg p-3">
                    <div className="card mx-auto" style={{width: "18rem"}}>
                        <div className="card-body">
                        <div className="icon-xl">
                            <FontAwesomeIcon icon={faSackDollar} />
                        </div>
                        <h5 className="card-title">Pide sin gastos de envío</h5>
                        <h6 className="card-subtitle mb-2 text-body-secondary">Tu primer pedido gratis y el siguiente 50% de descuento.</h6>
                        </div>
                    </div>
                    </div>
                    <div className="col-lg p-3">
                    <div className="card mx-auto" style={{width: "18rem"}}>
                        <div className="card-body">
                        <div className="icon-xl">
                            <FontAwesomeIcon icon={faUtensils} />
                        </div>
                        <h5 className="card-title">Trabaja con nosotros</h5>
                        <h6 className="card-subtitle mb-2 text-body-secondary">¿Eres un restaurante? Únete a pideREST para llegar más allá.</h6>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    )
}