from sourceConfig import config
from masterConfig import mconfig
import pdb
import pickle
def CrawlAll():
    allres =[]
    #pdb.set_trace();
    for k,v in config.items():
        if k not in mconfig['allowled']:
            continue
        seeds = v['seeds']
        handaler = v['handaler']
        for sd in seeds:
            data = handaler(sd)
            allres += data
    print 'Total', len(allres)
    return allres
    
def save(allres):
    with open('data.pkl', 'wb') as handle:
        pickle.dump(allres, handle)

        
#CrawlAll();