import React, { useContext, useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'
import { makeApiRequest } from '../api/api'
import { getAds, getFreshAds, getPersonalizedAds, getTrendingAds } from "../api/adsApi"
import "../css/dashboard.css"
import { UserContext } from '../Context/UserContext'
import { WebSocketContext } from  '../Context/WebSocketContext'
import AdsCarousel from './AdsCarousel'
import Searchbar from './Searchbar'
import Separator from './Separator'
import { useNavigate } from 'react-router-dom'
import Button from '@mui/material/Button';
import AdCategories from './AdCategories'
import { stepIconClasses } from '@mui/material'

const Dashboard = () => {
  const [ads, setAds] = useState([])
  const [freshAds, setFreshAds] = useState([])
  const [personalizedAds, setPersonalizedAds] = useState([])
  const [trendingAds, setTrendingAds] = useState([])
  const userInfo = useContext(UserContext)
  const [errorMessage, setErrorMessage] = useState("")
  const userId = userInfo?.userId
  const [selectedCategory, setSelectedCategory] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  

  function handleSearch(input) {
    console.log(input)
    if (input !== null && input.length > 0) {
      navigate(`/search?query=${encodeURIComponent(input)}`)
    }
    
  }



  // TODO: check whether concurrent
  useEffect(() => {
    let ignore = false
    console.log(userInfo)
    const fetchAds = async () => {
      setLoading(true)
      try {
        console.log("Fetching started at:", new Date().toISOString());

        const [trendingAds, personalizedAds, freshAds] = await Promise.all([
          getTrendingAds(20),
          userInfo.userId ? getPersonalizedAds(20) : [],
          getFreshAds(20)
        ])
        console.log("Fetching finished at:", new Date().toISOString());

        if (!ignore) {
          setTrendingAds(trendingAds)
          setPersonalizedAds(personalizedAds)
          setFreshAds(freshAds)
        }
       
        
      } catch (error) {
        console.error(error)
        setErrorMessage("Could not load the ads. Try again later")
      } finally {
        setLoading(false)
      }
      
    }

    fetchAds()

    return () => {
      ignore = true
    }

  }, [userId])


  return (
    <div className='dashboard__container'>
      <Header />
      
      <div className='dashboard'>
        
        <Searchbar onSearch={(input) => handleSearch(input)}/>

        <div className='categories__container'>
          <h3>All Categories</h3>
          <AdCategories selectedCategory={selectedCategory} onCategorySelected={(category) => setSelectedCategory(category)} jumpToSearch={true} />
          <Separator />
        </div>
        
        
        <div className='dashboard__ads-container'>
          <AdsCarousel loading={loading} ads={trendingAds} header="Currently trending"/>

          <Separator />

          <AdsCarousel loading={loading} ads={freshAds} header="Recently added"/>

          <Separator />
          
          {userInfo.userId && 
            <AdsCarousel loading={loading} ads={personalizedAds} header="For you"/> 
          }

          {/* <Ads ads={ads}/> */}
        </div>
        
      </div>
      
    </div>
  )
}

export default Dashboard
