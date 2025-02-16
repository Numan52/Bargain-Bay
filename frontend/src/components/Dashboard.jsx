import React, { useContext, useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'
import { getAds, getFreshAds, getPersonalizedAds, getTrendingAds, makeApiRequest } from '../api/api'
import "../css/dashboard.css"
import { UserContext } from '../Context/UserContext'
import { WebSocketContext } from  '../Context/WebSocketContext'
import AdsCarousel from './AdsCarousel'

const Dashboard = () => {
  const [ads, setAds] = useState([])
  const [freshAds, setFreshAds] = useState([])
  const [personalizedAds, setPersonalizedAds] = useState([])
  const [trendingAds, setTrendingAds] = useState([])
  const userInfo = useContext(UserContext)
  const [errorMessage, setErrorMessage] = useState("")
 

  console.log("dashboard user id: ", userInfo)

  console.log("ads", ads)





  // use Promise.all
  // TODO: check whether concurrent
  useEffect(() => {
    const fetchAds = async () => {
      try {
        console.log("Fetching started at:", new Date().toISOString());

        const [trendingAds, personalizedAds, freshAds] = await Promise.all([
          getTrendingAds(20),
          getPersonalizedAds(),
          getFreshAds()
        ])
        console.log("Fetching finished at:", new Date().toISOString());

        setTrendingAds(trendingAds)
        setPersonalizedAds(personalizedAds)
        setFreshAds(freshAds)
        
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
        <AdsCarousel ads={trendingAds} header="Currently trending"/>
        {/* <AdsCarousel ads={freshAds} header="Recently added"/>
        <AdsCarousel ads={personalizedAds} header="For you"/> */}

        {/* <Ads ads={ads}/> */}
      </div>
      
    </div>
  )
}

export default Dashboard
