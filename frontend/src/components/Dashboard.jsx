import React, { useEffect, useState } from 'react'
import Header from './Header'
import Ads from './Ads'

const Dashboard = () => {
  const [ads, setAds] = useState([])

  useEffect(() => {

    const fetchAds = async () => {
      
    
    }
    
  }, [])


  return (
    <div>
      <Header />
      
      <Ads ads={ads}/>
    </div>
  )
}

export default Dashboard
