import React, { useEffect } from 'react'
import "../css/ads.css"
import { Link } from 'react-router-dom'

const Ads = ({ads}) => {
  
  
    
  return (
    <div className='ads-container'> 
      {ads.map((ad) => (
        <Link to={`/ad/${ad.id}`} key={ad.id} className='ad-container' state={{ad}}>
          <img src={ad.imageUrls.length > 0 ? ad.imageUrls[0] : "/image.png"} alt="" className={ad.imageUrls.length > 0 ? "ad-image" : "ad-image--missing"}/> 
          
            <div className='ad-info'>
              <div>{ad.title}</div>
              <div className='ad-info-price'>€ {ad.price}</div>
            </div>

          
        </Link>
      ))}
    </div>
  )
}

export default Ads
