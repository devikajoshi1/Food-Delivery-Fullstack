import { Route, Router, Routes } from "react-router";
import Layout from "./components/Layout";
import RestaurantsPage from "./pages/RestaurantsPage";
import MenuPage from "./pages/MenuPage";

export default function App(){
  return(
    <Routes>
      <Route element={<Layout/>}>
        <Route path="/" element={<RestaurantsPage/>} />
        <Route path="/restaurants/:id" element={<MenuPage/>}/>
      </Route>
    </Routes>
  )
}