from flask import Flask, request
import pickle
import pdb
import json
data = None
with open('data.pkl', 'rb') as handle:
  data = pickle.load(handle)

#pdb.set_trace()

from flask import Flask, Response
app = Flask(__name__)

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
  app.run(host='0.0.0.0', port=7777, debug=True)
