function move_menu_back()
{
    const menu = document.getElementById("menu-b");
    menu.style.left = "-1000px";
}

function move_menu_forward()
{
    const menu = document.getElementById("menu-b");
    menu.style.left = "0px";
}

function move_menu_chat_back()
{
    const menu = document.getElementById("menu-chat-create");
    menu.style.left = "-1000px";
}

function move_menu_chat_forward()
{
    const menu = document.getElementById("menu-chat-create");
    menu.style.left = "0px";
}

function move_settings_menu_back()
{
    const menu = document.getElementById('settings');
    menu.style.right = "0px";
}

function move_settings_menu_forward()
{
    const menu = document.getElementById('settings');
    menu.style.right = "-1000px";
}

function add_member(member_id)
{
    const members_list = document.getElementById('members_id');
    members_list.setAttribute('value', members_list.value.concat(member_id + '/'));
}

