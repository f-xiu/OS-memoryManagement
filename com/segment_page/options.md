create process P1 1225 3333 2222

show process P1

destroy process P1

show memory

address P1 2 3234
/*请求的页不再内存中，发生缺页中断
IO: 将页框0内容写入外存。进程 P4 段(0) 页(0)
IO: 将进程 P4 段(2) 页(3) 读入页框 0 中
进程P4段(2) 段偏移(3234) 物理地址为：162*/

create process P5 1225 3333 2222 1232
create process P6 1225 333 14 1888
create process P7 1225 3333 222 1666
create process P8 1225 3333 2222 1566
create process P9 1225 3333 2222 1825
create process P10 1225 3333 2222 16380

address P10 3 15999

address P10 0 1200

address P10 3 10999