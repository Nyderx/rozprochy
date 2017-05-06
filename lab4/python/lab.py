import grpc
from hospital_pb2 import *
import hospital_pb2_grpc


def readResultRows():
    finished = False

    rows = []
    while not finished:
        paramName = input("Type in param name: ")
        value = int(input("Type in value: "))
        unit = input("Type in unit: ")
        rows.append(ResultRow(name=paramName, value=value, unit=unit))
        if input("Type y if u want to continue adding rows: ") is not "y":
            finished = True

    return rows


def readResultGroups():
    finished = False

    groups = []
    while not finished:
        name = input("Type in group name: ")

        groups.append(ResultRowsGroup(name=name, resultRows=readResultRows()))
        if input("Type y if u want to continue adding groups: ") is not "y":
            finished = True

    return groups


def readRecord():
    patientId = input("Type in patientId: ")
    doctorId = input("Type in doctorId: ")
    date = input("Type in date: ")

    return ResultsRecord(patient=patientId, doctor=doctorId, date=date, resultRowsGroups=readResultGroups())

def addRecord(stub, resultsRecord):
    if stub.AddResult(resultsRecord):
        print("Record added\n")
    else:
        print("Record could not be added\n")


def run():
    channel = grpc.insecure_channel('localhost:10000')
    stub =  hospital_pb2_grpc.LabServiceStub(channel)

    finished = False

    while not finished:
        addRecord(stub, readRecord())

        if input("Type y if u want to continue adding results") is not "y":
            finished = True


try:
    run()
except ValueError:
   print("Wrong type of value typed")
except grpc._channel._Rendezvous:
   print("Problem with connection")