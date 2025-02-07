import React, { useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import "../css/adDetails.css"
import Header from './Header'

const AdDetails = () => {
  const {adId} = useParams()
  const location = useLocation()
  const [ad, setAd] = useState(location.state?.ad || null)

  useEffect(() => {
    async function getAd(id) {
        const ad = await getAd(id)
        setAd(ad)
    }

    if (!ad) {
        getAd()
    }
    
  }, [ad])

  return (
    <>
        <Header />
        <div className='ad-details-ad-container'>
            <h2>{ad.title}</h2>
        </div>
    </>
    
  )
}

export default AdDetails
