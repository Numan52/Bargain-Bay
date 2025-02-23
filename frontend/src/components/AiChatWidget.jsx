import React, { useState } from 'react';
import "../css/chatWidget.css";
import askChatGpt from '../api/chatgptApi';

const AiChatWidget = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([
        {
            role: "assistant",
            content: "Hi! I am your AI Assistant. I can help you in the selling or buying process. Just ask me a question and also include the product ID relating to your question. \nFor example, try the following: 'How can I get a lower price on this product?'"
        }
    ]);
    const [adId, setAdId] = useState(null); // Input for ad ID
    const [input, setInput] = useState(""); // Input for user message


    const sendMessage = async () => {
        if (!input.trim()) return;

        // Add user message to chat
        const userMessage = { role: "user", content: input };
        setMessages((prev) => [...prev, userMessage]);
        setInput(""); 

        try {
            // Ask ChatGPT with message and adId
            const data = await askChatGpt({ message: input, adId });

            if (data && data.reply) {
                const aiMessage = { role: "assistant", content: data.reply };
                setMessages((prev) => [...prev, aiMessage]);
            }
        } catch (error) {
            console.error("Error getting AI response:", error);
            setMessages((prev) => [...prev, { role: "assistant", content: "Sorry, I couldn't process your request." }]);
        }
    };


    return (
      <div className="chat-container">
          {/* Floating Button */}
          <button className="chat-button" onClick={() => setIsOpen(!isOpen)}>💬</button>

          {/* Chat Window */}
          {isOpen && (
              <div className="chat-box">
                  <div className="chat-header">
                      <span>AI Assistant</span>
                      <button className="close-button" onClick={() => setIsOpen(false)}>✖</button>
                  </div>

                  <div className="chat-messages">
                      {messages.map((msg, idx) => (
                          <div key={idx} className={`chat-message ${msg.role}`}>
                              {msg.content}
                          </div>
                      ))}
                  </div>

                  {/* Ad ID Input */}
                  <input
                      type="text"
                      className="chats__chat-ad-id"
                      value={adId}
                      onChange={(e) => setAdId(e.target.value)}
                      placeholder="Enter Product ID (Optional)"
                  />

                  {/* Message Input */}
                  <div className="chat-input">
                      <input
                          type="text"
                          value={input}
                          onChange={(e) => setInput(e.target.value)}
                          placeholder="Type a message..."
                      />
                      <button onClick={sendMessage}>Send</button>
                  </div>
              </div>
          )}
      </div>
    );
};

export default AiChatWidget;
