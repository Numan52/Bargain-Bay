import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import "../css/adDetails.css"
import Header from './Header'
import { updateGuestActivity, updateUserActivity } from '../api/adsApi'
import { getUser } from "../api/userApi"
import { getAd } from "../api/adsApi"
import { UserContext } from '../Context/UserContext'
import { WebSocketContext } from '../Context/WebSocketContext'


const AdDetails = () => {
  const {adId} = useParams()
  const [ad, setAd] = useState(null)
  const [user, setUser] = useState(null)
  const [slideshowIndex, setSlideshowIndex] = useState(0)
  const [chatMessage, setChatMessage] = useState("")
  const userInfo = useContext(UserContext)
  const {publishMessage} = useContext(WebSocketContext)
  const navigate = useNavigate()
  console.log(JSON.stringify(userInfo))
  const [errorMessage, setErrorMessage] = useState("")
  const [successMessage, setSuccessMessage] = useState("")

  
  // update ad view count
  useEffect(() => {
    let viewedAds = localStorage.getItem("viewedAds") || []
  
    if (viewedAds.length < 1) {
      localStorage.setItem("viewedAds", [])
    } else {
      viewedAds = JSON.parse(viewedAds)
    }

    
    try {
      console.log("includes")
      if (userInfo.userId) {
        updateUserActivity(adId)
        console.log("marked ad as seen by user")
      } else {
        updateGuestActivity(adId)
        console.log("marked ad as seen by guest")
      }
      
      localStorage.setItem("viewedAds", JSON.stringify([...viewedAds, adId]))

    } catch (error) {
      console.log(error)
    }
    
    
  }, [adId, userInfo])


  useEffect(() => {
    async function fetchdata() { 
      try {
        const ad = await getAd(adId)
        setAd(ad)

        const user = await getUser(ad.userId)
        setUser(user)

      } catch (error) {
        console.log(error)
        navigate("/not-found")
      }
    }

    fetchdata()
    
  }, [adId])



  function sendMessage() {
    setErrorMessage("")
    setSuccessMessage("")
    if (!chatMessage.trim()) { 
      return
    }

    if(!userInfo.userId) {
      setErrorMessage("You need to be logged in to send messages.")
      setTimeout(() => {
        setErrorMessage("")
      }, 3000);
      return
    }

    const message = {
      senderId: userInfo.userId,
      receiverId: ad.userId,
      content: chatMessage,
      ad: {id: adId}
    }
    
    try {
      publishMessage(ad.userId, message)
    } catch (error) {
      console.log(error)
    }


    setSuccessMessage("Message was sent.")
    setTimeout(() => {
      setSuccessMessage("")
    }, 3000)
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
                    <img src={ad.imageUrls[slideshowIndex] || "/image.png"} alt="" />
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

                {ad.userId !== userInfo.userId && <div className='details__chat-container'>
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

                  {errorMessage &&
                    <div className='error-message'>
                      {errorMessage}
                    </div>
                  }
                  
                  {successMessage &&
                    <div className='success-message'>
                    {successMessage}
                  </div>
                  }
                  
                  
                </div>}

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

        {/* <div className='details__similar-ads-container'>
          Similar ads ..
        </div> */}
      </div>
      }
      
    </>
  )
}

export default AdDetails
