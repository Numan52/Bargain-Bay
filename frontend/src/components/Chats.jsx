import React, { useContext, useEffect, useRef, useState } from 'react'
import { UserContext } from '../Context/UserContext'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getAllContacts, getChatMessages, markChatAsSeen } from '../api/api'
import "../css/chats.css"
import Header from './Header'
import { WebSocketContext } from '../Context/WebSocketContext'
// import { RotatingLines } from 'react-loader-spinner'



// TODO: update last message and date when receiving message
const Chats = () => {
  const {chatId} = useParams()
  const {publishMessage, messages} = useContext(WebSocketContext)
  const userInfo = useContext(UserContext)
  const [allContacts, setAllContacts] = useState([])
  const [selectedChatId, setSelectedChatId] = useState(null)
  const [chatMessages, setChatMessages] = useState([])
 
  const [inputText, setInputText] = useState("")
  const [loading, setLoading] = useState(false);

  const textareaRef = useRef(null)
  
  const navigate = useNavigate()

  console.log("all contacts: ", allContacts)
  console.log("chat messages: ", chatMessages)

  useEffect(() => {
    async function fetchContacts() {
        try {
            const json = await getAllContacts(userInfo.username)
            setAllContacts(json)
        } catch (error) {
            console.log(error)
        }
    } 

    fetchContacts()
    
  }, [userInfo])


  useEffect(() => {
    const mainTextarea = textareaRef.current

    if (!mainTextarea) return;

    const handleInput = (textarea) => {
      textarea.style.height = 'auto'; // Reset height to auto
      textarea.style.height = `${textarea.scrollHeight - 20}px`; // Set height to the scroll height
    }


    mainTextarea.addEventListener("input", () => handleInput(mainTextarea))


    return () => {
      mainTextarea.removeEventListener("input", () => handleInput(mainTextarea))
    }
  }, [inputText])


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


  async function handleContactClick(chatId) {

    if (chatId !== selectedChatId) {
      setSelectedChatId(chatId)
      const chat = await fetchMessages(chatId) 
      setChatMessages(chat.messages)
    }

    updateUnreadCounts(chatId)
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


  function fetchMessages(chatId) {
    return getChatMessages(chatId)
  }


  function sendMessage() {
    if (!inputText) {
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

  return (
    <>
      <Header />
      <div className='messages-content-container'>
        <div className='messages-contacts-container'>
          {allContacts.map((contact) => (
            <div key={contact.chatId} className='chats__contact-container' onClick={() => handleContactClick(contact.chatId)}>
              <img src="/user.png" alt="user image" />
              <div className='chats__contact-details'>
                <div className='chats__contact_username-container'>
                  <span className={`chats__contact-username ${contact.chatId === selectedChatId ? 'chats__contact-selected' : ''}`}>{contact.username}</span>
                  {contact.unreadMessages > 0 && <span className='chats__contact-unseen-messages'>{contact.unreadMessages > 100 ? '100+' : contact.unreadMessages}</span>}
                </div>
                
                <div className='chats_contact-last-message-container'>
                  <span>{contact.lastMessage}</span>
                  <span>{contact.lastMessageTime}</span>
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
          <div className='chats__messages-container'>
            {chatMessages.map((message => (
              <div className={`chats__message ${message.senderId === userInfo.userId ? 'chats__own-message' : 'chats__other-message' }`} key={message.id}>
                <div className='chats__message-content'>{message.content}</div>
                <span className='chats__message-date'>
                  {new Date(message.sentAt).toLocaleTimeString("en-GB", {hour:"2-digit", minute:"2-digit"})}
                </span>
              </div>
            )))}
            
          </div>

          {selectedChatId  &&
              <div className='chats__send-message-container'>
                  <textarea 
                      ref={textareaRef} 
                      placeholder="Type a message"
                      rows={1}
                      value={inputText}
                      onChange={(e) => setInputText(e.target.value)}
                  >

              </textarea>
                  <img src="/send.png" alt="" onClick={sendMessage}/>
              </div>
          }
        </div>
        

        
      </div>
    </>
    
    
  )
}

export default Chats
