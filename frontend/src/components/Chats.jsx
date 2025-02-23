import React, { useContext, useEffect, useRef, useState } from 'react'
import { UserContext } from '../Context/UserContext'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getAllContacts, getChatMessages, markChatAsSeen } from "../api/chatsApi"
import "../css/chats.css"
import Header from './Header'
import { WebSocketContext } from '../Context/WebSocketContext'
import { formatDateTime, DateToTime } from '../util.js/dateUtils'
import AiChatWidget from './AiChatWidget'
// import { RotatingLines } from 'react-loader-spinner'



// TODO: update last message and date when receiving message
const Chats = () => {
  const {chatId} = useParams()
  const {publishMessage, messages} = useContext(WebSocketContext)
  const userInfo = useContext(UserContext)
  const [allContacts, setAllContacts] = useState([])
  const [selectedChatId, setSelectedChatId] = useState(null)
  const [chatMessages, setChatMessages] = useState([])
  const chatRef = useRef(null)
  const [inputText, setInputText] = useState("")
  const [loading, setLoading] = useState(false);

  const textareaRef = useRef(null)
  
  const navigate = useNavigate()

  console.log("all contacts: ", allContacts)
  console.log("chat messages: ", chatMessages)

  useEffect(() => {
    async function init() {
      setInputText("")
      try {
          const json = await getAllContacts(userInfo.username)
          setAllContacts(json)
          console.log("json: ", json)
          if (json.length > 0) {
            if (chatId) {
              handleContactSelection(chatId, true)
            } else {
              handleContactSelection(json[0].chatId, true)
            }
          }
      } catch (error) {
          console.log(error)
      }
    } 

    init()

  
  }, [userInfo, chatId])


  function handleMsgInput(e) {
    setInputText(e.target.value)

    textareaRef.current.style.height = 'auto'; // Reset height to auto
    textareaRef.current.style.height = `${textareaRef.current.scrollHeight - 20}px`; // Set height to the scroll height
  }


  useEffect(() => {
    scrollToBottom()
  }, [chatMessages])
   

  useEffect(() => {
    if (messages.length > 0) {
      const newMessage = messages[messages.length - 1]; // Get last received message
      if (newMessage.chatId === selectedChatId) {
        setChatMessages(prev => [...prev, newMessage]); // Add it to chat
      } else {
        setAllContacts((prevContacts) => 
          prevContacts.map((contact) => {
            if (contact.chatId === newMessage.chatId ) {
              return { ...contact, unreadMessages: contact.unreadMessages + 1}
            }
            return contact
          })
        )
      }
    }
  }, [messages]); // Runs when new messages arrive


  async function handleContactSelection(chatId, isPageLoad) {
    try {
      if (chatId !== selectedChatId) {
        setSelectedChatId(chatId)
        const chat = await getChatMessages(chatId) 
        setChatMessages(chat.messages)
        navigate(`/chats/${chatId}`); // Ensure URL updates
      }
      if (!isPageLoad) {
        updateUnreadCounts(chatId)
      }
      
    } catch (error) {
      console.log(error)
    }
    
  }


  async function updateUnreadCounts(chatId) {
    try {
      await markChatAsSeen(chatId)
    } catch (error) {
      console.log(error) 
    }

    setAllContacts((prevContacts) => 
      prevContacts.map((contact) => 
        contact.chatId === chatId ? 
            {...contact, unreadMessages: 0} :
            contact 
      )
    )
  }


  function scrollToBottom() {
    chatRef.current.scrollTop = chatRef.current.scrollHeight
  }


  function sendMessage() {
    if (!inputText.trim()) {
      return
    }


    console.log("sending message")
    try {
      const receiver = allContacts.find((contact) => contact.chatId === selectedChatId)
      const message = {
        senderId: userInfo.userId,
        receiverId: receiver.userId,
        content: inputText,
        chatId: selectedChatId
      }

      publishMessage(receiver.userId, message)
      // setChatMessages((prev) => [...prev, ])
    } catch (error) {
      console.log(error)
    }
    setInputText("")
  }


  function renderMessages() {
    
    let lastMessage = null
    let lastMessageDate = null

    return chatMessages.map((message) => {
      
      const messageDate = new Date(message.sentAt).toLocaleDateString()
      const fromToday = new Date().toLocaleDateString() === messageDate
      const showDate = messageDate !== lastMessageDate
      

      let displayAdInfo = message?.ad !== null && message.ad.id !== lastMessage.ad?.id
      

      lastMessageDate = messageDate
      lastMessage = message

      const sender = allContacts.find((contact) => contact.userId === message.senderId)
      const senderName = sender?.username ?? "You"
      const verb = sender ? "is" : "are"
      const adInfoMessage = `${senderName} ${verb} interested in the following product: `

      return (
        <>
          {showDate && 
            <div className='chats__date-separator'>
              {fromToday ? "Today" : messageDate}
            </div>
          }
          {displayAdInfo &&
            <div className='chats__ad-info'>
              {adInfoMessage} 
              <Link className='chats__ad-info-title' to={`/ad/${message.ad.id}`}>
                {message.ad?.title}
              </Link>
              
            </div>
          }
          <div className={`chats__message ${message.senderId === userInfo.userId ? 'chats__own-message' : 'chats__other-message' }`} key={message.id}>
            <div className='chats__message-content'>{message.content}</div>
            <span className='chats__message-date'>
              {DateToTime(message.sentAt)}
            </span>
          </div>
        </>
        
      )})
  }
  


  // TODO: insert date for messages
  return (
    <>
      <Header />
      <div className='chats__container'>
        <h2>Your Messages</h2>


        <div className='chats__main'>
            <div className='messages-content-container'>
            
            <div className='messages-contacts-container'>
              {allContacts.map((contact) => (
                <div key={contact.chatId} className='chats__contact-container' onClick={() => handleContactSelection(contact.chatId, false)}>
                  <img src="/user.png" alt="user image" />
                  <div className='chats__contact-details'>
                    <div className='chats__contact_username-container'>
                      <span className={`chats__contact-username ${contact.chatId === selectedChatId ? 'chats__contact-selected' : ''}`}>{contact.username}</span>
                      {contact.unreadMessages > 0 && <span className='chats__contact-unseen-messages'>{contact.unreadMessages > 100 ? '100+' : contact.unreadMessages}</span>}
                    </div>
                    
                    <div className='chats_contact-last-message-container'>
                      <span>{contact.lastMessage}</span>
                      <span>{formatDateTime(contact.lastMessageTime)}</span>
                    </div>
                    
                  </div>
                </div>
              ))}

              
              {/* {loading &&
                      <div className='feed-loading-info'>
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
                        />
                        
                      </div>
              } */}
              
            </div>

            <div className='chats__messages-area-container'>
              <div className='chats__messages-container' ref={chatRef}>
                {renderMessages()}
              </div>

              {selectedChatId  &&
                  <div className='chats__send-message-container'>
                      <textarea 
                          ref={textareaRef} 
                          placeholder="Type a message"
                          rows={1}
                          value={inputText}
                          onChange={(e) => handleMsgInput(e)}
                      >

                  </textarea>
                      <img src="/send.png" alt="" onClick={sendMessage}/>
                  </div>
              }
            </div>
            

            
          </div>

          <div className='chats__ai-container'>
            <AiChatWidget />
          </div>
        </div>

        
        
        

      </div>
      
      
    </>
    
    
  )
}

export default Chats
