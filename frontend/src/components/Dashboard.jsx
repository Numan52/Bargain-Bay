import React, { useContext, useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'
import { getAds, makeApiRequest } from '../api/api'
import "../css/dashboard.css"
import { UserContext } from '../Context/UserContext'
import { WebSocketContext } from  '../Context/WebSocketContext'

const Dashboard = () => {
  const [ads, setAds] = useState([])
  const userInfo = useContext(UserContext)
  const [errorMessage, setErrorMessage] = useState("")
  const {messages} = useContext(WebSocketContext)

  console.log("dashboard user id: ", userInfo)

  console.log("ads", ads)

  console.log("messages in dashboard: ", messages)

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
