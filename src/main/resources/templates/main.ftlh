<#include "./parts/security.ftlh">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <link href="/static/css/text.css" type="text/css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.5/dist/umd/popper.min.js" integrity="sha384-Xe+8cL9oJa6tN/veChSP7q+mnSPaj5Bcu9mPX5F5xIGE0DVittaqT5lorf0EI7Vk" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.min.js" integrity="sha384-kjU+l4N0Yf4ZOJErLsIcvOU2qSb74wXpOhqTvwVx3OElZRweTnQ6d31fXEoRD1Jy" crossorigin="anonymous"></script>
    <script src="/static/js/test.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="/static/js/chat.js"></script>
    <title>CustomChat</title>
</head>
<body style="overflow-y: hidden; overflow-x: hidden;" onload="initializeSpacer()">
<div class="container-fluid"style="margin-right: 0px;margin-left: 0px; background-color: #E4FAFA; text-align: center; align-self: center; height: 100vh; position: relative;">
    <#if id??>
        <input type="hidden" id="userID" value="${id}">
    </#if>
    <div class="col-2 back-menu" id="menu-b" style="left:-1000px;">
        <img onclick="move_menu_back()" src="/static/img/menu.svg" style="height: 64px; width: 64px; margin: 5px;" align="left">
        <div style="overflow-y: scroll; overflow-x: hidden">
            <#if chats??>
                <#list chats as chat>
                    <div id="${chat.id}"  class="col-2 select-chat-menu-item">
                        <img onclick="addActiveChat(${chat.id})" src="/static/img/pin.svg" class="chat-picture">
                        <input type="hidden" value="${chat.name}" id="chatName${chat.id}">
                        <div>${chat.name}</div>
                        <#if chat.dialog>
                            <input type="hidden" id="group-chat/${chat.id}"/>
                        <#else>
                            <input type="hidden" id="chat/${chat.id}"/>
                        </#if>
                    </div>
                </#list>
            </#if>
        </div>
    </div>
    <div class="col-2 back-menu" id="menu-chat-create" style="left:-1000px; flex-direction: column;">
        <img onclick="move_menu_chat_back()" src="/static/img/menu.svg" style="height: 64px; width: 64px; margin: 5px;" align="left">
        <form action="/createChat" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <label for="name">Name of chat</label>
            <input class="form-control col-1" id="name" type="text" name="name" minlength="1" style="margin-top: 5px; margin-bottom: 5px;">
            Image: <button class="chat-image-create" style="margin-top: 5px; margin-bottom: 5px;">
                <input type="file" name="chat_image" style="opacity: 0; width: 50px; height: 50px;">
            </button>
            <input type="text" value="" name="members_usernames" id="members_id" hidden/>
            <input name="form-control" type="text" id="member" list="users" placeholder="Type username for search">
            <datalist id="users">
                <#list users as user>
                    <option value="${user.username}"></option>
                </#list>
            </datalist>
            <div class="row justify-content-center">
                <div onclick="add_member(document.getElementById('member').value)" class="btn btn-outline-dark m-1 col-8" style="display: flex; text-align: center;">Add member</div>
            </div>
            <div class="row justify-content-center">
                <button type="submit" class="btn btn-outline-dark m-1 col-8" style="display: flex; text-align: center;">Create chat</button>
            </div>
        </form>
        <div id="paste_chats_here" class="col-2 select-chat-menu-item" hidden>
            <img class="chat-picture" src="/static/img/face.png" align="left">
            <div>Max Shevchuk</div>
        </div>
    </div>
    <div class="row" style="position: relative";>
        <div class="col-2 left-menu-bar">
            <img onclick="move_menu_forward()" src="/static/img/menu.svg" align="left" style="height: 64px; width: 64px; margin: 5px;">
            <img onclick="move_menu_chat_forward()" src="/static/img/create-chat.svg" align="left" style="height: 64px; width: 64px; margin: 5px;">
        </div>
        <#if activeChats??>
            <#list activeChats as chat>
                <div class="col-2 active-chat-bar">
                    <div class="strange">
                        <div>
                            <img src="../static/img/face.png" class="active-chat-img">
                        </div>
                        <div>
                            <div class="active-chat-name"><div class="mover">${chat.name}</div></div>
                        </div>
                    </div>
                    <img src="../static/img/close_active_chat.svg" class="active-chat-img-close" align="right" id="${chat.id}" onclick="removeActiveChat(this)">
                    <img src="../static/img/enter.svg" class="active-chat-img-close" align="center" onclick="location.href = '/chat/${chat.id}'">
                </div>
            </#list>
        </#if>
        <div id="paste-active-chat-here" hidden></div>
        <div class="col-8" id="spacer">
        </div>
        <div class="col-2 settings-and-profile" id="settings" style="right: -1000px; flex-direction: column;">
            <img onclick="move_settings_menu_forward()" src="/static/img/menu.svg" style="height: 64px; width: 64px; margin: 5px;">
            <form action="/createChat" method="post">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <label for="name">Name of chat</label>
                <input class="form-control col-1" id="name" type="text" name="name" minlength="1" style="margin-top: 5px; margin-bottom: 5px;">
                Image: <button class="chat-image-create" style="margin-top: 5px; margin-bottom: 5px;">
                    <input type="file" name="chat_image" style="opacity: 0; width: 50px; height: 50px;">
                </button>
                <input type="text" value="" name="members_usernames" id="members_id" hidden/>
                <input name="form-control" type="text" id="member" list="users" placeholder="Type username for search">
                <datalist id="users">
                    <#list users as user>
                        <option value="${user.username}"></option>
                    </#list>
                </datalist>
                <div class="row justify-content-center">
                    <div onclick="add_member(document.getElementById('member').value)" class="btn btn-outline-dark m-1 col-8" style="display: flex; text-align: center;">Add member</div>
                </div>
                <div class="row justify-content-center">
                    <button type="submit" class="btn btn-outline-dark m-1 col-8" style="display: flex; text-align: center;">Create chat</button>
                </div>
            </form>
            <div id="paste_chats_here" class="col-2 select-chat-menu-item" hidden>
                <img class="chat-picture" src="/static/img/face.png" align="left">
                <div>Max Shevchuk</div>
            </div>
        </div>

        <div class="col-2 right-menu-bar" style="position: relative;">
            <img class="user-self-img" src="/static/img/face.png" align="left">
            <div class="right-menu-bar-button-block">
                <#if known>
                    <div onclick="move_settings_menu_back()" class="right-menu-bar-settings">Settings</div>
                </#if>
                <form action="/login" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <#if known>
                        <button type="submit" class="right-menu-bar-loginout">Logout</button>
                        <#else>
                            <button type="submit" class="right-menu-bar-loginout">Login</button>
                    </#if>
                </form>
            </div>
        </div>
    </div>
    <div class="row" style="background-color: #D1FCD0; height: 100%; position: relative;">
        <div class="col-2 group-notif-block">
            <div class="group-notif-label">
                <div>Group notifications</div>
            </div>
            <div class="group-notif" id = "paste_group_notif_here" hidden>
                <p class="group-notif-in-label">Group: IPZ-2022</p>
                <p class="group-notif-in-label">Author: Max SHevchuk</p>
                <div class="group-notif-message">Lorem ipsum dolor sit amet,Lorem ipsum dolor sit ametLorem ipsum dolor sit amet,Lorem ipsum dolor sit ametLorem ipsum dolor sit amet,Lorem ipsum dolor sit</div>
            </div>
        </div>
        <div class="col-8">
            <div class="chat-space" id="chat-block">
                <#if chat??>
                    <#if messages??>
                    <#list messages as message>
                        <#if message.sender.id == id>
                            <div class="message-block" style="margin-left: 50%">
                                <img class="message-img" src="/static/img/face.png">
                                <div class="message-sender-name">${message.sender.username}</div>
                                <div class="message-text">${message.text}</div>
                            </div>
                            <#else>
                                <div class="message-block" style="margin-right: 50%">
                                    <img class="message-img" src="/static/img/face.png">
                                    <div class="message-sender-name">${message.sender.username}</div>
                                    <div class="message-text">${message.text}</div>
                                </div>
                        </#if>
                    </#list>
                    </#if>
                </#if>
                <div class="message-block" id="paste_message_here" hidden>
                </div>
            </div>
            <div class="write-block">
                <div class="row justify-content-center write-frame">
                    <div class = "col-1">
                        <button class="attachment">
                            <input type="file" id="file" style="opacity: 0; width: 50px; height: 50px;">
                        </button>
                    </div>
                    <div class="col-8" >
                        <textarea class="form-control write-textarea" id="message-text" maxlength="1024"></textarea>
                    </div>
                    <#if id??>
                        <input type="hidden" value="${id}" id="sender"/>
                    </#if>
                    <#if chat??>
                        <input type="hidden" value="${chat.id}" id="chat"/>
                    </#if>
                    <div class = "col-1">
                        <div onclick="sendMessage()" class="send"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-2 personal-notif-block">
            <div class="personal-notif-label">
                Personal notifications
            </div>
            <div class="personal-notif" id="paste_personal_notif_here" hidden>
                <p class="personal-notif-in-label">Author: Max SHevchuk</p>
                <div class="personal-notif-message">Lorem ipsum dolor sit amet,Lorem ipsum dolor sit ametLorem ipsum dolor sit amet,Lorem ipsum dolor sit ametLorem ipsum dolor sit amet,Lorem ipsum dolor sit</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>