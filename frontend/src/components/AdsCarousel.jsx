import React, { useRef } from 'react'
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import "../css/adsCarousel.css"
import { Link } from 'react-router-dom';
import { RotatingLines } from 'react-loader-spinner'

const AdsCarousel = ({ads, header, loading}) => {
  const adsCarouselRef = useRef(null)
  
  
  function scrollCarousel(direction) {
    const scrollAmount = 400
    adsCarouselRef.current.scrollBy({
      left: direction === "left" ? -scrollAmount : scrollAmount,
      behaviour: "smooth"
    })
  }


  
  return (
    <div>
      <h3 className='ads__section__header'>{header}</h3>

      {loading ? 
          <RotatingLines
            visible={true}
            height="80"
            width="80"
            color="grey"
            strokeWidth="3"
            animationDuration="0.75"
            ariaLabel="rotating-lines-loading"
            wrapperStyle={{}}
            wrapperClass=""
        /> :
        <div className='ads-section__container' >
          <button className='ads-section__arrow' onClick={() => scrollCarousel("left")}>
            <FaChevronLeft size={24}/>
          </button>
          
          <div className='ads-section__ads-carousel' ref={adsCarouselRef}>
            {ads.map((ad) => 
              <Link to={`/ad/${ad.id}`} className='ads-section__ad' key={ad.id}>
                <img src={ad.imageUrls.length > 0 ? ad.imageUrls[0] : "/image.png"} alt="" />
                <div className='ads-section__ad-title'>
                  {ad.title.length > 35 ? ad.title.substring(0, 35) + "..." : ad.title}
                </div>
                <div className='ads-section__ad-price'>€ {ad.price}</div>
              </Link>
            )}
          </div>

          <button className='ads-section__arrow' onClick={() => scrollCarousel("right")}>
            <FaChevronRight size={24}/>
          </button>
        </div>
        
        }
      
    </div>
      

      
    
  )
}

export default AdsCarousel
