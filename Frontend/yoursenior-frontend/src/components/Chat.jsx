import { useState, useEffect, useRef } from 'react';
import api from '../api/axios';

function Chat({ bookingId, senderRole }) {
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState('');
  const bottomRef = useRef(null);

  const loadMessages = async () => {
    try {
      const res = await api.get(`/messages/${bookingId}`);
      setMessages(res.data);
    } catch (err) {
      // silent fail on poll — avoids spamming errors every few seconds
    }
  };

  useEffect(() => {
    loadMessages();
    const interval = setInterval(loadMessages, 4000); // poll every 4s
    return () => clearInterval(interval);
  }, [bookingId]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const sendMessage = async () => {
    if (!text.trim()) return;
    try {
      await api.post(`/messages/${bookingId}`, { content: text }, { params: { senderRole } });
      setText('');
      loadMessages();
    } catch (err) {
      // could surface an error state here if needed
    }
  };

  return (
    <div className="chat-box">
      <div className="chat-messages">
        {messages.length === 0 && <p className="chat-empty">No messages yet — say hello!</p>}
        {messages.map((m) => (
          <div
            key={m.id}
            className={`chat-bubble ${m.senderRole === senderRole ? 'chat-mine' : 'chat-theirs'}`}
          >
            <p>{m.content}</p>
            <span className="chat-time">
              {new Date(m.sentAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
            </span>
          </div>
        ))}
        <div ref={bottomRef} />
      </div>

      <div className="chat-input-row">
        <input
          placeholder="Type a message..."
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
        />
        <button className="btn-primary" onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}

export default Chat;