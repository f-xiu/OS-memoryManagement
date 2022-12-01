create process p1 1232 2332 313 13 123

show process p1

address p1 0 323
address p1 1 2330

destroy process p1

bug1:
    address p1 0 323
    发生缺段
    实际物理地址为:1232
    address p1 0 323
    发生缺段
    实际物理地址为:2464
    show memory
    段	开始地址	长度	状态
    1      -1	 1232	p1
    2      -1	 1232	p1
    3      -1	63072	Empty	