import './App.css';

import Inicio from './Inicio'
import Restaurantes from './Restaurantes'
import RestauranteOpiniones from './RestauranteOpiniones'

import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Layout from './Layout';
import { useState, useEffect } from 'react';

export default function App() {


  const [userLogged,setUserLogged] = useState(null);


  useEffect(()=>{

      const loggedInUser = localStorage.getItem("user");
      if(!loggedInUser) {

          fetch("http://localhost:3000/users/auth",{
              credentials: "include"
          })
          .then(res => res.json())
          .then(data => {
            const user = JSON.stringify(data);
            localStorage.setItem("user", user)
            setUserLogged(user)
          });
      }else{
        setUserLogged(loggedInUser)
      }

      

      

  })

  return (
    
    <BrowserRouter>
       <Routes>
        <Route path="/" element={<Layout user={userLogged} />}>
          <Route index element={<Inicio/>} />
          <Route path="restaurantes" element={<Restaurantes/>} />
          <Route path="opiniones/:id" element={<RestauranteOpiniones/>} />

        </Route>
      </Routes>
    </BrowserRouter>
  );
}