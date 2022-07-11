    var socket = new SockJS('/gs-guide-websocket');
    var stompClient = Stomp.over(socket);
    window.onload = connect();

    function scroll() {
        const box = document.getElementById('chat-block');
        box.scrollTop = box.scrollHeight;
    }

    function connect() {
    stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function(messageOutput) {
    showMessageOutput(JSON.parse(messageOutput.body));
});
});
}

    function disconnect() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
}

    function sendMessage() {
    var sender = document.getElementById('sender').value;
    var text = document.getElementById('message-text').value;
    var chat = document.getElementById('chat').value;
    var file = document.getElementById('file').value;
    stompClient.send("/app/hello", {},
    JSON.stringify({'sender':sender, 'text':text, 'chat':chat} ));
}

    function addActiveChat(id) {
        var place_to_paste = document.getElementById('paste-active-chat-here');
            if (place_to_paste.parentNode.childElementCount != 9) {
                var active_chat_block = document.createElement('div');
                active_chat_block.classList.add('col-2');
                active_chat_block.classList.add('active-chat-bar');

                var strange_block = document.createElement('div');
                strange_block.classList.add('strange');

                var chat_img = document.createElement('img');
                chat_img.classList.add('active-chat-img');
                chat_img.src = '../static/img/face.png';

                strange_block.appendChild(chat_img);

                var out_div = document.createElement('div');

                var parent_name = document.createElement('div');
                parent_name.classList.add('active-chat-name');

                var mover = document.createElement('div');
                mover.classList.add('mover');
                mover.innerHTML =  document.getElementById('chatName'+id).value;

                parent_name.appendChild(mover);
                out_div.appendChild(parent_name);
                strange_block.appendChild(out_div);

                var enter_chat = document.createElement('img');
                enter_chat.classList.add('active-chat-img-close');
                enter_chat.setAttribute('onclick', 'location.href = \'/chat/'+id+'\'');
                enter_chat.setAttribute('id', 'chat'+id);
                enter_chat.setAttribute('src', './static/img/enter.svg');
                enter_chat.setAttribute('align', 'center');

                var close_chat = document.createElement('img');
                close_chat.classList.add('active-chat-img-close');
                close_chat.setAttribute('onclick', 'deleteChat('+id+')');
                close_chat.setAttribute('id', id);
                close_chat.setAttribute('src', './static/img/close_active_chat.svg');
                close_chat.setAttribute('align', 'right');
                close_chat.setAttribute('onclick', 'removeActiveChat(this)')

                active_chat_block.appendChild(strange_block);
                active_chat_block.appendChild(enter_chat);
                active_chat_block.appendChild(close_chat);


                place_to_paste.insertAdjacentElement('beforebegin', active_chat_block);

                var spacer = document.getElementById('spacer');

                if (spacer.classList.contains('col-8'))
                {
                    spacer.classList.remove('col-8');
                    spacer.classList.add('col-6');
                }
                else if (spacer.classList.contains('col-6'))
                {
                    spacer.classList.remove('col-6');
                    spacer.classList.add('col-4');
                }
                else if (spacer.classList.contains('col-4'))
                {
                    spacer.classList.remove('col-4');
                    spacer.classList.add('col-2');
                }
                else if (spacer.classList.contains('col-2')) {
                    spacer.style.display = 'none';
                }

                stompClient.send('/app/addActiveChat', {}, JSON.stringify({'chatId':id, 'chatName':document.getElementById('chatName'+id).value, 'userId': document.getElementById('userID').value}));
            }
}

    function showMessageOutput(messageOutput) {
        if (document.getElementById('chat') != null)
        {
            if (messageOutput.chat == document.getElementById('chat').value)
            {
                var response = document.getElementById('paste_message_here');
                var chat_block = document.createElement('div');
                chat_block.classList.add('message-block');
                var image = document.createElement('img');
                image.classList.add('message-img');
                image.src = '/static/img/face.png';

                var username = document.createElement('div');
                username.classList.add('message-sender-name');

                var text = document.createElement('div');
                text.classList.add('message-text');

                chat_block.appendChild(image);
                username.appendChild(document.createTextNode(messageOutput.sender_username));
                text.appendChild(document.createTextNode(messageOutput.text));

                chat_block.appendChild(username);

                chat_block.appendChild(text);

                if (messageOutput.sender == document.getElementById('sender').value)
                {
                    chat_block.style.marginLeft = '50%';
                }
                else
                {
                    chat_block.style.marginRight = '50%';
                }
                response.insertAdjacentElement('beforebegin', chat_block);

                document.getElementById('message-text').value = '';

                const box = document.getElementById('chat-block');
                box.scrollTop = box.scrollHeight;
            }
            else if (document.getElementById('chat/' + messageOutput.chat) != null)
            {
                createPersonalNotification(messageOutput);
            }
            else if (document.getElementById('group-chat/' + messageOutput.chat) != null)
            {
                createGroupNotifications(messageOutput)
            }
        }
        else if (document.getElementById('chat/' + messageOutput.chat) != null)
        {
            createPersonalNotification(messageOutput);
        }
        else if (document.getElementById('group-chat/' + messageOutput.chat) != null)
        {
            createGroupNotifications(messageOutput)
        }
}

    function createPersonalNotification(messageOutput) {
    var notification_block = document.createElement('div');
    notification_block.classList.add('personal-notif');

    var author_label = document.createElement('p');
    author_label.classList.add('personal-notif-in-label');
    author_label.innerHTML += messageOutput.sender_username;

    var notif_text = document.createElement('div');
    notif_text.classList.add('personal-notif-message');
    notif_text.innerHTML += messageOutput.text;

    notification_block.appendChild(author_label);
    notification_block.appendChild(notif_text);

    notification_block.setAttribute('onclick', 'location.href = \'/chat/'+messageOutput.chat+'\'');

    var place_to_paste = document.getElementById('paste_personal_notif_here');
    place_to_paste.insertAdjacentElement('beforebegin', notification_block);
}

    function createGroupNotifications(messageOutput) {
    var notification_block = document.createElement('div');
    notification_block.classList.add('personal-notif');

    var author_label = document.createElement('p');
    author_label.classList.add('personal-notif-in-label');
    author_label.innerHTML += 'Sender ' + messageOutput.sender_username;

    var chat_label = document.createElement('p');
    chat_label.classList.add('group-notif-in-label');
    chat_label.innerHTML += 'Chat ' + messageOutput.chat_name;

    var notif_text = document.createElement('div');
    notif_text.classList.add('personal-notif-message');
    notif_text.innerHTML += messageOutput.text;

    notification_block.appendChild(chat_label);
    notification_block.appendChild(author_label);
    notification_block.appendChild(notif_text);

    notification_block.setAttribute('onclick', 'location.href = \'/chat/'+messageOutput.chat+'\'');

    var place_to_paste = document.getElementById('paste_group_notif_here');
    place_to_paste.insertAdjacentElement('beforebegin', notification_block);
}

    function initializeSpacer() {
        var spacer = document.getElementById('spacer');
        if (spacer.parentNode.childElementCount == 6) {
            spacer.classList.remove('col-8');
            spacer.classList.add('col-6');
        }
        else if (spacer.parentNode.childElementCount == 7) {
            spacer.classList.remove('col-8');
            spacer.classList.add('col-4');
        }
        else if (spacer.parentNode.childElementCount == 8) {
            spacer.classList.remove('col-8');
            spacer.classList.add('col-2');
        }
        else if (spacer.parentNode.childElementCount == 9) {
            spacer.classList.remove('col-8');
            spacer.classList.add('col-2');
            spacer.style.display = 'none';
        }
}

    function removeActiveChat(element) {
    element.parentNode.remove();
    var spacer = document.getElementById('spacer');

    if (spacer.style.display == 'none')
    {
        spacer.style.display = '';
    }
    else {
        if (spacer.classList.contains('col-2')) {
            spacer.classList.remove('col-2');
            spacer.classList.add('col-4');
        }
        else if (spacer.classList.contains('col-4')) {
            spacer.classList.remove('col-4');
            spacer.classList.add('col-6');
        }
        else if (spacer.classList.contains('col-6')) {
            spacer.classList.remove('col-6');
            spacer.classList.add('col-8');
        }
    }
    stompClient.send('/app/removeActiveChat', {}, JSON.stringify({'chatId': element.id, 'userId': document.getElementById('sender').value}));
}





