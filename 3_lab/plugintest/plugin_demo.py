import os
import pathlib
import importlib


def myfactory(moduleName):
    module = importlib.import_module("plugins."+moduleName)
    return getattr(module, '__init__')
    # return globals()[moduleName].__init__


def printGreeting(animal):
    print("%s POZDRAVLJA %s", animal.name(animal), animal.greet(animal))


def printMenu(animal):
    print("%s voli %s", animal.name(), animal.menu())


def test():
    pets = []
    # obiđi svaku datoteku kazala plugins
    for mymodule in os.listdir(pathlib.Path(__file__).parent / 'plugins'):
        moduleName, moduleExt = os.path.splitext(mymodule)
        # ako se radi o datoteci s Pythonskim kodom ...
        if moduleExt == '.py':
            # instanciraj ljubimca ...
            ljubimac = myfactory(moduleName)('Ljubimac '+str(len(pets)))
            print(ljubimac.name(ljubimac))
            # ... i dodaj ga u listu ljubimaca
            pets.append(ljubimac)

    # ispiši ljubimce
    for pet in pets:
        printGreeting(pet)
        printMenu(pet)


test()
