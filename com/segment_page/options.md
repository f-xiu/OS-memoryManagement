create process P1 1225 3333 2222

show process P1

destroy process P1

show memory

address P1 2 3234
/*请求的页不再内存中，发生缺页中断
IO: 将页框0内容写入外存。进程 P4 段(0) 页(0)
IO: 将进程 P4 段(2) 页(3) 读入页框 0 中
进程P4段(2) 段偏移(3234) 物理地址为：162*/