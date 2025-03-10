import { useEffect, useState } from 'react'
import './css/app.css'
import Dashboard from './components/Dashboard'
import { BrowserRouter, Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import Login from './components/Login'
import Register from './components/Register'
import { checkJwtExpired, getDecodedJwt } from './util.js/jwtUtils'
import CreateAd from './components/CreateAd'
import CreateAdSuccess from './components/CreateAdSuccess'
import AdDetails from './components/AdDetails'
import { UserContext } from './Context/UserContext'
import { getUserId } from "./api/userApi"
import {WebSocketProvider} from './Context/WebSocketContext'
import Chats from './components/Chats'
import SearchResults from './components/SearchResults'
import SearchResultsPage from './components/SearchResultsPage'
import NotFound from './components/NotFound'

function App() {
  const [token, setToken] = useState(localStorage.getItem("token")) || null
  const [jwtMessage, setJwtMessage] = useState("")
  const [user, setUser] = useState({})

  console.log("user: ", user)

  useEffect(() => {
    const token = localStorage.getItem('token');
    setToken(token)
  }, []);


  useEffect(() => {
    if (!token) {
      return
    }
    
    async function getId(username) {
      try {
        const json = await getUserId(username)
        console.log("json: ", json)
        setUser((prev) => ({
          ...prev,
          userId: json.userId
        }))

        return json.userId
      } catch (error) {
        console.log("error getting user id: ", error)
      }
    }

    const username = getDecodedJwt(token).sub
    setUser((prev) => ({
      ...prev, 
      username
    }))

    getId(username)
  }, [token])


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
      <UserContext.Provider value={user}>
        <WebSocketProvider>
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
              path="/chats/:chatId" 
              element={token ? <Chats /> : <Navigate to="/login" replace />} 
            >
            </Route>

            <Route
              path="/chats/" 
              element={token ? <Chats /> : <Navigate to="/login" replace />} 
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


            <Route
              path="/search" 
              element={<SearchResultsPage />} 
            >
            </Route>

            <Route path="*" element={<NotFound />} />

          </Routes> 
        </WebSocketProvider>
        
      </UserContext.Provider>
    </>
  )
}

export default App
