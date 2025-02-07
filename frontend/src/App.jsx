import { useEffect, useState } from 'react'
import './css/app.css'
import Dashboard from './components/Dashboard'
import { BrowserRouter, Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import Login from './components/Login'
import Register from './components/Register'
import { checkJwtExpired } from './jwtUtils'
import CreateAd from './components/CreateAd'
import CreateAdSuccess from './components/CreateAdSuccess'
import AdDetails from './components/AdDetails'

function App() {
  const [token, setToken] = useState(null)
  const [jwtMessage, setJwtMessage] = useState("")

  useEffect(() => {
    const token = localStorage.getItem('token');
    setToken(token)
  }, []);


  useEffect(() => {
    const intervalId = setInterval(() => {
      const token = localStorage.getItem("token");
  
      if (!token) {
        setToken(""); 
        localStorage.removeItem("token");
      } else if (checkJwtExpired(token)) { 
        setJwtMessage("Your session expired. Please log in again.");
        setToken(""); 
        localStorage.removeItem("token");
      }
  
    }, 5000); // Check every 5 seconds
  
    return () => clearInterval(intervalId);
  }, []);

  
  return (
    <>
      <Routes>
        <Route
          path="/register" 
          element={token ? <Navigate to="/" replace /> : <Register />}
        >
        </Route>

        <Route 
          path='/login'
          element={token ? <Navigate to="/" replace /> : <Login setToken={setToken} jwtMessage={jwtMessage}/>}
        >
        </Route>

        <Route
          path="/" 
          element={<Dashboard />} 
        >
        </Route>

        <Route
          path="/create-ad" 
          element={token ? <CreateAd /> : <Navigate to="/login" replace />} 
        >
        </Route>


        <Route
          path="/ad/:adId" 
          element={<AdDetails />} 
        >
        </Route>

        <Route
          path="/success" 
          element={token ? <CreateAdSuccess /> : <Navigate to="/login" replace />} 
        >
        </Route>

      </Routes> 
      
    </>
  )
}

export default App
