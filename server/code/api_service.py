from flask import Flask, request,render_template
import pickle
import pdb
import json





##################  INIT DATABASE ############################
import database
store = database.Store(); #return single ton..

###############################################################





###############  Multi thread to get the data ##############
"""
import threading
import crawler
def schedule():
    # do something here ...
    print '*'*50
    print 'Running Scheduler....'
    import time
    print 'Time:', time.ctime() # 'Mon Oct 18 13:35:29 2010'
    print '*'*50
    crawler.UpdateDataBase();
    print '*'*50
    # call f() again in 60 seconds
    threading.Timer(60*60*4, schedule).start() #Run every 4 hours
"""
###############################################################



##########################  We server ################################
from flask import Flask, Response
app = Flask(__name__)

@app.route('/news')
def hello_world():
    try:
        #pdb.set_trace();
        page = 1   if not request.args.get('page') else int(request.args.get('page') )
        limit = 50 if not request.args.get('limit') else int(request.args.get('limit') )
        query = '' if not request.args.get('query') else request.args.get('query')
        topic = '' if not request.args.get('topic') else request.args.get('topic')
        source = '' if not request.args.get('source') else request.args.get('source')
        day = '' if not request.args.get('date') else request.args.get('date') #dd-mm-yyyy
        global store
        result =[]
        if len(query.strip()) != 0:
            result = store.get(page, limit)
        if len(topic.strip()) != 0:
            result = store.getByCat(topic, page, limit)
        if len(source.strip()) != 0:
            result = store.getBySource(source, page, limit)
        if len(day.strip()) != 0:
            result = store.getByDate(day, 1, 500) # We want to get all the news for the day..
        else:
            result = store.get(page, limit)
           
        return Response(json.dumps({'result':result,'status':'OK','count':len(result),'msg':'Data Returned with page'+str(page)+' and limit '+str(limit)+'.'}), mimetype ='application/json')
            
    except Exception,e:
       print e
       return Response(json.dumps({'result':[],'status':'ERROR','msg':'Somethong goes wrong ! Talk to dipankar!'+str(e)}), mimetype ='application/json')
#########################################################################     

@app.route("/reports", methods=['POST'])
def post_crash(): 
    data = dict(request.json)
    res = store.insert_crash( data)
    if(res == True):
        return Response(json.dumps({'status':'OK'}));
    else:
        return Response(json.dumps({'status':'ERROR'}));
    
@app.route("/reports", methods=['GET'])
def get_reports(): 
    #pdb.set_trace()
    crashes = store.get_crash(1,1000)
    count = len(crashes)
    return render_template('reports.html',**locals())      

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5555, debug=True,threaded=True)
