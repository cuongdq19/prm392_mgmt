const express = require('express');
const bodyParser = require('body-parser');
const { createServer } = require('http');
const { Server } = require('socket.io');

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const httpServer = createServer(app);
const io = new Server(httpServer, {
  cors: {
    origin: '*',
  },
});

app.get('', (req, res) => {
  res.json('Alive');
});

io.on('connect', (socket) => {
  console.log(`Connection: SocketId = ${socket.id}`);

  var username = '';

  socket.on('subscribe', (data) => {
    console.log('Subscribe triggered');

    var roomData = JSON.parse(data);

    username = roomData.username;
    var roomName = 'admin';

    socket.join(`${roomName}`);

    io.to(`${roomName}`).emit('newUserToChatRoom', data);
  });

  socket.on('unsubscribe', (data) => {
    console.log('Unsubscribe triggered');

    var roomName = 'admin';

    console.log(`Username: ${username} leaved room name: ${roomName}`);
    socket.broadcast.to(`${roomName}`).emit('userLeftChatRoom', username);
    socket.leave(`${roomName}`);
  });

  socket.on('newMessage', (data) => {
    console.log('newMessage triggered');

    var roomData = JSON.parse(data);
    var username = roomData.username;
    var messageContent = roomData.content;
    var roomName = 'admin';

    console.log(`[Room Number ${roomName}] ${username}: ${messageContent}`);

    const chatData = {
      username: username,
      content: messageContent,
      roomName: roomName,
    };

    socket.broadcast
      .to(`${roomName}`)
      .emit('updateChat', JSON.stringify(chatData));
  });

  socket.on('disconnect', () => {
    console.log('One of sockets disconnected from our server.');
  });
});

httpServer.listen(8080);
