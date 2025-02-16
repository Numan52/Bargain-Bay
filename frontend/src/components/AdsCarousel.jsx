import React, { useRef } from 'react'
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import "../css/adsCarousel.css"
import { Link } from 'react-router-dom';

const AdsCarousel = ({ads, header}) => {
  const adsCarouselRef = useRef(null)

  function scrollCarousel(direction) {
    const scrollAmount = 400
    adsCarouselRef.current.scrollBy({
      left: direction === "left" ? -scrollAmount : scrollAmount,
      behaviour: "smooth"
    })
  }


  console.log("ads: ", ads)
  return (
    <div>
      <h3>{header}</h3>
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
    </div>
      

      
    
  )
}

export default AdsCarousel
