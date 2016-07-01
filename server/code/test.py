import threading

def f():
    # do something here ...
    print 'hello'
    # call f() again in 60 seconds
    threading.Timer(2, f).start()

# start calling f now and every 60 sec thereafter
f();
print 'hello2222'