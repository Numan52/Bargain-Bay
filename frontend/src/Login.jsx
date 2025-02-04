import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import "./css/login.css"
import PropTypes from 'prop-types';
const apiUrl = import.meta.env.VITE_API_URL

const Login = ({setToken, jwtMessage}) => {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [errorMessage, setErrorMessage] = useState("")

  const navigate = useNavigate()

  console.log("jwtMessage: ", jwtMessage)
  async function handleLogin(e) {
    e.preventDefault()
    setErrorMessage("")

    try {
      const response = await fetch(`${apiUrl}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json"},
        body: JSON.stringify({username: username, password: password})
      })
    
      if(response.ok) {
        const data = await response.json()
        console.log(data)
        console.log("login successful") 
        localStorage.setItem("token", data.jwt);
        setToken(data.jwt)
      } else {
        const message = await response.text()
        if (message.toLowerCase().includes("incorrect username or password")) {
          setErrorMessage(message)
          console.log(true)
        } else {
          setErrorMessage("An error ocurred. Please try again later")
        }
        console.log("response: " + message)
      }
  
    } catch (error) {
      console.log("error while logging in: ", error)
      setErrorMessage("An error ocurred. Please try again later")
    }
  }

  return (
    <div className='login-container'>
      
      <form onSubmit={handleLogin}>
        <div className='login-header'>Login</div>
        <label htmlFor="login-name">Username</label>
        <input
            id='login-name'
            type="text" 
            placeholder='Username'
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required  
        />

        <label htmlFor="login-password">Password</label>
        <input 
            id='login-password'
            type="password" 
            placeholder='Password'
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required  
        />

        <div className='login-button-container'>
          <button className='login-button' type='submit'>
            Login
          </button>
          <div className='register-message'>Don't have an Account? <Link to="/register">Sign Up</Link></div>
        </div>

      </form>

      {errorMessage && 
        <div style={{color:"red", marginTop:"15px", fontSize:"20px"}}> 
          {errorMessage}
        </div>
      }
      {jwtMessage && 
        <div style={{color:"red", marginTop:"15px", fontSize:"20px"}}> 
          {jwtMessage}
        </div>
      }
    </div>
  )
}


Login.propTypes = {
    setToken: PropTypes.func.isRequired
}

export default Login
