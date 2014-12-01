CREATE TABLE sqllist(
id integer PRIMARY KEY,
sql text,
md5 text,
env text,
status integer ,
seq integer ,
cost  integer ,
result text,
color text,
addtime TIMESTAMP default (datetime('now', 'localtime'))
);