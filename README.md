### Содержимое WEB-части
* libraries.jar -> Библиотеки клиента.
* Forge.jar -> Minecraft Forge.
* extra.jar -> Запасной джарник для OptiFine, PlayerApi, GuiApi и т.д.
* client.zip -> Содержит bin/natives для lwjgl, config для модов.  
Добавлен assets.zip Звуки для клиентов 1.6.+, должен быть одинаковым размером во всех клиентах на сайте версии 1.6.+)
* Список серверов теперь редактируется на сайте servers.php.
* Дописаны скрипты для авторизации 1.7.2-1.8.0.
Ссылки на новые скрипты указываем в классе YggdrasilMinecraftSessionService.class
"http://minecraft/site/"
"http://minecraft/site/j.php"
"http://minecraft/site/h.php"

Новая структура клиента должна быть такой
clients/assets/ ресурс файлы. При режиме zip clients/assets.zip
clients/voxelaria/config.zip конфиги модов и ресурскаки, расспаковывается в корень папки клиента.
clients/voxelaria/bin/ jar файлы клиента + папка natives, можно
использовать подпапки bin/libraries/ и тд.
clients/voxelaria/mods/  zip-jar файлы, модов, можно использовать
подпапки mods/lib/lib.jar
clients/voxelaria/coremods/ zip-jar файлы коремодов (используется
только устаревшими версиями minecraft)
