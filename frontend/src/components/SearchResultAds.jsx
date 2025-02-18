import React from 'react'
import { Link } from 'react-router-dom'
import "../css/searchResults.css"
import Separator from './Separator'

const SearchResultAds = ({ads}) => {
  return (
    <div className='search-result-ads__ads-container'>
        
      {ads.length > 0 && ads.map((ad, index) => (
        <div key={ad.id}>
            <Link to={`/ad/${ad.id}`}  className='search-result-ads__ad-container' >
                <div className='search-result-ads__image-container'>
                    <img src={ad.imageUrls.length > 0 ? ad.imageUrls[0] : "/image.png"} alt="" className={ad.imageUrls.length > 0 ? "ad-image" : "ad-image--missing"}/> 
                </div>
                
                <div className='search-result-ads__ad-info'>
                    <div>{ad.title}</div>
                    <div className='ad-info-price'>€ {ad.price}</div>
                </div>

                
            </Link>
            
            {index !== ads.length - 1 && <Separator />}
        </div>
            
      ))}
    </div>
  )
}

export default SearchResultAds
