import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import "../css/login.css"
import PropTypes from 'prop-types';
import { loginUser } from '../api/userApi';
import Header from './Header';
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
      const json = await loginUser(username, password)
      localStorage.setItem("token", json.jwt)
      setToken(json.jwt)
    } catch (error) {
      setErrorMessage(error.message)
    }
  }

  return (
    <>
      <Header />
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
    </>
    
  )
}


Login.propTypes = {
    setToken: PropTypes.func.isRequired
}

export default Login
