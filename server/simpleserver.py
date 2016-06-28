from flask import Flask, request
import pickle
import pdb
import json
import crawler
data = [{'state':'No data'}]
#with open('data.pkl', 'rb') as handle:
#  data = pickle.load(handle)

#pdb.set_trace()

from flask import Flask, Response
app = Flask(__name__)

###############  Multi thread to get the data ##############
import threading
def schedule():
    # do something here ...
    print '*'*50
    print 'Running Scheduler....'
    import time
    print 'Time:', time.ctime() # 'Mon Oct 18 13:35:29 2010'
    print '*'*50
    global data
    data = crawler.CrawlAll();
    print '*'*50
    # call f() again in 60 seconds
    threading.Timer(60*60*2, schedule).start() #Run every 2 hours


@app.route('/news')
def hello_world():
    try:
      if data:
        page = 0 if not request.args.get('page') else int(request.args.get('page') )
        limit = 50 if not request.args.get('limit') else int(request.args.get('limit') )
        query = '' if not request.args.get('query') else request.args.get('query')
        
        return Response(json.dumps({'result':data[page*limit:(page+1)*limit],'status':'OK'}),  mimetype='application/json')
      else:
        return Response(json.dumps({'result':None,'status':'ERR'}),  mimetype='application/json')
    except Exception,e:
       print e
if __name__ == '__main__':
    schedule()
    app.run(host='0.0.0.0', port=7777, debug=True)
