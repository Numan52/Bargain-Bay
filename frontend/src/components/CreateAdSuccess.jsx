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
      <h2>{`You Ad ${location.state?.title} was successfully posted!`}</h2>
      <Link to={"/"}>Back to home</Link>
    </div>
  )
}

export default CreateAdSuccess
