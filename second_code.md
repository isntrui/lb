### Первым был реализован сценарий авторизации. Тут не возникло никаких трудностей. Первично я написал конфигурацию, после чего занялся написанием сервиса и контроллера для неё.

### После авторизации был реализован таск-менеджер: управление задачами, создание и отслеживание. Тут так же не возникло никаких трудностей (в целом со всем бэкэндом трудностей не возникало)

### Далее была реализована загрузка единиц контента — звонков, креативов и текстов.

### Позже было подключено хранилище S3 на базе Yandex Cloud и миддлвэйр под него. Тут возникли лишь трудности на стороне API AWS — Яндекс использует SDK Amazon'а, интеграцию для которого нужно отдельно "костылить", но, к счастью, я не первый столкнулся с этой проблемой и на Хабре уже было решение :)

###### ~~А вообще труднее всего было выбрать лицензию под проект~~