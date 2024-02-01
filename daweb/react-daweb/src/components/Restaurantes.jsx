import Container from 'react-bootstrap/Container';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass,faGear,faClose } from '@fortawesome/free-solid-svg-icons';
import { Alert, Card, Col, Row } from 'react-bootstrap';
import { Fragment, useEffect } from 'react';
import { useState } from 'react';
import Restaurante from './Restaurante';
import Spinner from 'react-bootstrap/Spinner';
import Form from 'react-bootstrap/Form';
import { useLocation } from 'react-router-dom';

import { MapContainer, TileLayer, useMap,Marker,Popup, Circle} from 'react-leaflet'
import { useMapEvents } from 'react-leaflet/hooks'





export default function Restaurantes(props) {

  
    const [restaurantes, setRestaurantes] = useState(null);

    var initial_q = '';
    let { search } = useLocation();

    const query = new URLSearchParams(search);

    if(query.get('q')){
      initial_q = query.get('q');
    }

    const [q, setQ] = useState(initial_q);

    const [showSettings, setShowSettings] = useState(false);

    const [ minVal, setMin ] = useState(0);
    const [ maxVal, setMax ] = useState(5);


  
    const [ selectedPosition, setSelectedPosition ] = useState(null);
    const [ maxKms, setMaxKms ] = useState(1);

    const [ciudad, setCiudad] = useState('');

    const handleInputQ = (event) => {
      setQ(event.target.value);
    };

    const toggleShowSettings = (event) => {
      setShowSettings(!showSettings)
    };

    const handleInputCiudad = (event) => {
      setCiudad(event.target.value);
    };

    const handleInputMaxKms = (event) => {
      setMaxKms(event.target.value);
    };

    const handleClickMap = (event) => {
      setSelectedPosition([event.latlng.lat,event.latlng.lng])
    };

    const changeMin =(event) => {
      var nuevoMinimo=event.target.value
      if(nuevoMinimo > maxVal)
      setMin(maxVal)
      else setMin(nuevoMinimo)
      
    }

    const changeMax =(event) => {
      var nuevoMaximo=event.target.value
      if(nuevoMaximo < minVal)
      setMax(minVal)
      else setMax(nuevoMaximo)
    }

    const getURLRestaurante = (id)=>{
      return `http://localhost:3000/restaurantes/${id}`
    }
    const clearQ = ()=>{setQ('')}


    function MapEvents({onClick}){
      const map = useMapEvents({
        click: (e) => {
          onClick(e)
        }
      });

      return null;
  
    }

    
    

    useEffect(()=>{

      
      
      
     

      fetch("http://localhost:3000/restaurantes/api/search?"  + new URLSearchParams({
          q: q,
          ciudad: ciudad,
          minVal: minVal,
          maxVal: maxVal,
          lat: selectedPosition ? selectedPosition[0]:'',
          lng: selectedPosition ? selectedPosition[1]:'',
          radius: maxKms
      }),{
        credentials: "include"
      })
      .then(res => {
        if(res.status!=200){
          throw new Error(res.status)
        }
        return res.json()
      })
      .then(data => setRestaurantes(data))
      .catch(function(error){
        localStorage.removeItem("user");
        window.location.replace('http://localhost:3000/login');
      });

    },[q,ciudad,minVal,maxVal,maxKms,selectedPosition])

    return (
      
      <Container className="mt-5">

          <div className="input-group mb-3">
            <input 
                type="text"
                className="form-control"
                placeholder="El restaurante que tanto estabas buscando..."
                onChange={handleInputQ}
                value={q} />

                <button onClick={clearQ} className="btn btn-dark btn-lg" type="button" id="button-addon2">
                  <FontAwesomeIcon icon={faClose}/>
                </button>

                <button onClick={toggleShowSettings} className="btn btn-dark btn-lg" type="button" id="button-addon2">
                  <FontAwesomeIcon icon={faGear}/>
                </button>
                <button  className="btn btn-dark btn-lg" type="button" id="button-addon2" >
                  <FontAwesomeIcon icon={faMagnifyingGlass}/>
                </button>
          </div>

          { showSettings ?
              <Card className="mb-3 fade-in">
                <Card.Body>
                  <Row>
                    <Col xs={4}>

                      <h4>
                        Introduce una ciudad
                      </h4>

                      <div className="input-group mb-3">

                        <div style={{width: "100%"}}>
                          <label htmlFor="ciudadInput">Ciudad</label>
                        </div>
                        <div>
                          <input 
                              id='ciudadInput'
                              type="text"
                              className="form-control mt-2"
                              value={ciudad}
                              placeholder="Murcia, Madrid, Alicante..."
                              onChange={handleInputCiudad} />
                        </div>

                      </div>

                    </Col>

                    <Col xs={4}>

                      <h4>
                        Selecciona una zona
                      </h4>

                      <p>
                        Haz click en el mapa para filtrar restaurantes por ubicación.
                      </p>
                      
                      <MapContainer center={[37.9926807,-1.1311537]} zoom={12} style={{"width":"100%","height":"250px","overflow":"hidden"}}>
                        <TileLayer
                          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                        <MapEvents onClick={handleClickMap}/>

                        { selectedPosition ? 
                          
                          <Fragment>
                            <Marker
                              position={selectedPosition}
                            />
                            <Circle center={selectedPosition} radius={maxKms*1000} />
                            
                          </Fragment>


                          :''
                        }

                      </MapContainer>

                        <div className="mt-3" style={{width: "100%"}}>
                          <label htmlFor="ciudadInput">Kilometros distancia máxima</label>
                        </div>

                      <input 
                              id='ciudadInput'
                              type="text"
                              className="form-control mt-2"
                              value={maxKms}
                              placeholder="30,40,50"
                              onChange={handleInputMaxKms} />
                    </Col>

                    <Col xs={4}>
                      <h4>
                        Selecciona rango de valoraciones
                      </h4>
                      <Row>
                        <Col xs={9}>
                          <div>
                        <Form.Label>Min</Form.Label>
                          <Form.Range
                          value={minVal}
                          onChange={changeMin}
                          min={0}
                          max={5}
                          />
                          </div>
                          </Col>
                          <Col xs={3}>
                          <div className='margin-auto'>
                            <span >{minVal}</span>
                            </div>
                          </Col>
                          
                      </Row>
                      <Row>
                      <Col xs={9}>
                        <Form.Label>Max</Form.Label>
                          <Form.Range
                          value={maxVal}
                          onChange={changeMax}
                          min={0}
                          max={5}
                          />
                          </Col>
                          <Col xs={3}>
                            {maxVal}
                          </Col>
                      </Row>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            : ''
          }
          
          {restaurantes 
              ? 
                (restaurantes.length>0
                ?
                <Fragment>
                <Row>
                  {restaurantes.map((item) => (
                    <Col key={item.id} xs={3}>
                      <Restaurante id={item.id} nombre={item.nombre} ciudad={item.ciudad}/>
                    </Col>
                  ))}
                </Row>

                <div>
                  <h3 className="text-white mb-3">Mapa de restaurantes</h3>

                  <MapContainer center={[37.9926807,-1.1311537]} zoom={12} style={{"width":"100%","height":"500px","overflow":"hidden","margin-bottom":"30px"}}>
                        <TileLayer
                          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                        
                        {restaurantes.map((item) => (
                          <Marker
                            position={[item.lat,item.lng]}>

                              <Popup>
                                <b>{item.nombre}</b>
                                <div>
                                  <a href={getURLRestaurante(item.id)} className="btn btn-link btn-sm">Abrir restaurante</a>
                                </div>
                              </Popup>
                          </Marker>
                        ))}

                        { selectedPosition ? 
                          
                          <Fragment>
                            
                            <Circle center={selectedPosition} radius={maxKms*1000} />
                            
                          </Fragment>


                          :''
                        }

                      </MapContainer>
                </div>



                      
                </Fragment>
                :
                <Alert variant='info'>No hay restaurantes que cumplan los criterios de búsqueda</Alert>)
              :  <Spinner  variant="black" />
          }


                      
          
      </Container>
    );
  }
    