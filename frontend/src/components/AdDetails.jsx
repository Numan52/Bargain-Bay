import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import "../css/adDetails.css"
import Header from './Header'
import { getAd, getUser } from '../api/api'
import { UserIdContext } from '../Context/UserContext'
import { WebSocketContext } from '../Context/WebSocketContext'


const AdDetails = () => {
  const {adId} = useParams()
  const [ad, setAd] = useState(null)
  const [user, setUser] = useState(null)
  const [slideshowIndex, setSlideshowIndex] = useState(0)
  const [chatMessage, setChatMessage] = useState("")
  const userInfo = useContext(UserIdContext)
  const {publishMessage} = useContext(WebSocketContext)
  
  useEffect(() => {
    async function fetchdata() { 
      try {
        const ad = await getAd(adId)
        setAd(ad)

        const user = await getUser(ad.userId)
        setUser(user)

      } catch (error) {
        console.log(error)
      }
    }

    fetchdata()
    
  }, [adId])



  function sendMessage() {
    const message = {
      senderId: userInfo.userId,
      receiverId: ad.userId,
      content: chatMessage 
    }
    
    try {
      publishMessage(ad.userId, message)
    } catch (error) {
      console.log(error)
    }
  }


  return (
    <>
      <Header />
      {ad != null && user != null && 
        <div className='details__container'>
      
        <div className='details__ad-container'>
            <div className='details__title-container'>
              <h2>{ad.title}</h2>
            </div>
            
            <div className='details__ad-overview-container'>
              <div className='details__ad-images-container'>
                  <div className='details__ad-current-image-container'>
                    <img src={ad.imageUrls[slideshowIndex]} alt="" />
                  </div>

                  <div className='details__ad-all-images-container'>
                    <img className='details__arrow' src="/left-arrow.png" alt="" 
                      onClick={() => {
                        setSlideshowIndex((prev) => Math.max(prev - 1, 0))
                      }}
                    />
                    <div className='details__ad-all-images-overview'>
                      {ad.imageUrls.map((imageUrl, index) => (
                        <img 
                          src={`${imageUrl}`} 
                          alt="" 
                          key={imageUrl}
                          onClick={() => setSlideshowIndex(index)} 
                          className={index === slideshowIndex ? "image-selected" : ""}
                        />
                      ))}
                    </div>

                    <img className='details__arrow' src="/right-arrow.png" alt="" onClick={() => setSlideshowIndex((prev) => Math.min(prev + 1, ad.imageUrls.length - 1) )}/>
                  </div>
              </div>
              <div className='details__ad-user-container'>
                <div className='details__price-container'>
                  <span className='details__ad-price'>€ {ad.price}</span>
                  <span>Price</span>
                </div>

                <div className='details__ad-user-details-container'>
                  <div className='details__ad-user'>
                    <img className='' src="/user.png" alt="" />
                    <span>{user.username}</span>
                  </div>

                </div>

                <div className='details__chat-container'>
                  <p>Send a message to the seller</p>
                  <textarea 
                    name="chat" 
                    id="details__textarea"
                    value={chatMessage}
                    onChange={(e) => setChatMessage(e.target.value)}
                  >
              
                  </textarea>
                  <button className='details__send-message-btn' onClick={sendMessage}>
                    <img src="/chat.png" alt="" />
                    Send Message
                  </button>
                </div>

              </div>
            </div>

            <div className='details__details-container'>
              <div className='details__condition-container'>
                <h3>Details</h3>
                <span>Condition: {ad.condition}</span>
              </div>

              <div className='details__description-container'>
                <h3>Description</h3>
                <p>{ad.description}</p>
              </div>
            </div>

        </div>

        <div className='details__similar-ads-container'>
          Similar ads ..
        </div>
      </div>
      }
      
    </>
  )
}

export default AdDetails
