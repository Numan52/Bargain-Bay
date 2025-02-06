import React, { useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'
import { getAds, makeApiRequest } from '../api/api'

const Dashboard = () => {
  const [ads, setAds] = useState([])
  const [errorMessage, setErrorMessage] = useState("")
  console.log("ads", ads)

  useEffect(() => {

    const fetchAds = async () => {
      try {
        const adsJson = await getAds()
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
      
      <Ads ads={ads}/>
    </div>
  )
}

export default Dashboard
