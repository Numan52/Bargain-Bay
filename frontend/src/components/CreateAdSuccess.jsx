import React, { useEffect } from 'react'
import Header from './Header'
import { useLocation, Link, useNavigate } from 'react-router-dom'

const CreateAdSuccess = () => {
  const location = useLocation()
  const navigate = useNavigate();

  useEffect(() => {
    if (!location.state?.title) {
      navigate("/")
    }
  }, [location, navigate])

  return (
    <div>
      <Header />
      <div className='create-ad__success'>
        <h2>{`Your Ad "${location.state?.title}" was successfully posted!`}</h2>
        <Link to={"/"}>Back to home</Link>
      </div>
      
    </div>
  )
}

export default CreateAdSuccess
