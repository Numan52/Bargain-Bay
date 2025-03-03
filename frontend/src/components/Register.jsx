import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'

import "../css/register.css"
import { registerUser } from "../api/userApi"
import Header from './Header'
const apiUrl = import.meta.env.VITE_API_URL

const Register = () => {
  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [email, setEmail] = useState("")
  const [secondPassword, setSecondPassword] = useState("")
  const [errorMessage, setErrorMessage] = useState("")

  const navigate = useNavigate()

  async function handleRegister(e) {
    e.preventDefault()
    setErrorMessage("")

    if (password !== secondPassword) {
      setErrorMessage("The two passwords don't match")
      return;
    }
    try {
      await registerUser(firstName, lastName, username, password, email)
      navigate("/login")
    } catch (error) {
      setErrorMessage(error.message)
    }
  }

  return (
    <>
      <Header />
      <div className='register-container'>
      
      <form onSubmit={handleRegister}>
        <div className='register-header'>Register</div>

        <label htmlFor="register-first-name">First Name</label>
        <input 
          id='register-first-name'
          type="text" 
          placeholder='First Name'
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          required  
        />


        <label htmlFor="register-last-name">Last Name</label>
        <input 
          id='register-last-name'
          type="text" 
          placeholder='Last Name'
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          required  
        />

        <label htmlFor="register-username">Username</label>
        <input 
          id='register-username'
          type="text" 
          placeholder='Username'
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required  
        />

        <label htmlFor="register-email">Email Address</label>
        <input 
          id='register-email'
          type="text" 
          placeholder='Email Address'
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required  
        />

        <label htmlFor="register-password">Password</label>
        <input 
          id='register-password'
          type="password" 
          placeholder='Password'
          
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required  
        />

        <label htmlFor="register-password-confirm">Confirm Password</label>
        <input
          id='register-password-confirm'
          type="password" 
          placeholder='Confirm your Password'
          value={secondPassword}
          onChange={(e) => setSecondPassword(e.target.value)}
          required  
        />
        
        <div className='register-button-container'>
          <button className='register-button' type='submit'>
            Register
          </button>
          <div className='login-message'>Already have an Account? <Link to="/login">Sign In</Link></div>
        </div>

      </form>
      

      {errorMessage && 
        <div style={{color:"red", marginTop:"15px"}}> 
          {errorMessage}
        </div>
      }
    </div>
    </>
    
  )
}

export default Register