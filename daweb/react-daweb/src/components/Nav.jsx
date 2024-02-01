import Nav from 'react-bootstrap/Nav';

import Navbar from 'react-bootstrap/Navbar';
import Container from 'react-bootstrap/Container';


import { LinkContainer } from 'react-router-bootstrap';



export default function Nav2({user_str}) {


    const user = JSON.parse(user_str);



    return (
        <Navbar expand="lg" variant="dark">
            <Container fluid>
                <LinkContainer to="/">
                    <Navbar.Brand>
                        <img src='/images/logo-horizontal-min.svg' />
                    </Navbar.Brand>
                </LinkContainer>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto" >

                    <LinkContainer to="/">
                        <Nav.Link eventKey="1">Inicio</Nav.Link>
                    </LinkContainer>

                    {
                        user && user.rol == "GESTOR"
                            ? <Nav.Link href="http://localhost:3000/gestion">Gestión</Nav.Link>
                            : ''
                    }
                        
                    <LinkContainer to="/restaurantes">
                        <Nav.Link eventKey="2">Restaurantes</Nav.Link>
                    </LinkContainer>
                </Nav>
                <Nav>
                    {
                        user
                            ? <Nav.Link href="http://localhost:3000/profile">Bienvenido, {JSON.parse(localStorage.getItem("user")).usuario}</Nav.Link>
                            : <Nav.Link href="http://localhost:3000/login">Iniciar sesión</Nav.Link>
                    }
                </Nav>
                    
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
  }
  