import { Outlet } from "react-router-dom";
import Nav  from "./Nav";

export default function Layout({user}) {
    return (
      <div>
        <Nav user_str={user}/>
        <Outlet />
      </div>

      
    );
  }
  