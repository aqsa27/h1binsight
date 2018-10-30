# -*- coding: utf-8 -*-
from ParseInput import ParseInput
from Top10list import Top10list
import sys
    
def main():
    separator = ';' #global separator declaration 
    inputfile, outputoccupation, outputstate = sys.argv[1],sys.argv[2],sys.argv[3] #input, output file paths
    InputHolder = ParseInput(inputfile, separator)
    certified_count,occupation_dict, states_dict = InputHolder.getData() #get distribution of occupation and states
    Toppers = Top10list(certified_count,occupation_dict, states_dict,separator, outputstate,outputoccupation)
    Toppers.top10Occupation() #writes top 10 occupation to a file
    Toppers.top10State() #writes top 10 states to a file

if __name__ == "__main__":
    main()

