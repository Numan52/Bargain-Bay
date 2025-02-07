import React, { useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'
import { getAds, makeApiRequest } from '../api/api'
import "../css/dashboard.css"

const Dashboard = () => {
  const [ads, setAds] = useState([])
  const [errorMessage, setErrorMessage] = useState("")
  console.log("ads", ads)

  useEffect(() => {

    const fetchAds = async () => {
      try {
        const adsJson = await getAds(0, 10)
        setAds(adsJson)
      } catch (error) {
        console.log(error)
        setErrorMessage("Could not load the ads. Try again later")
      }
      
    }

    fetchAds()
    
  }, [])


  return (
    <div>
      <Header />
      
      <div className='dashboard'>
        <Ads ads={ads}/>
      </div>
      
    </div>
  )
}

export default Dashboard
