#/bin/bash
echo Starting Pull
mkdir pull
cd pull
wget -r -p -e robots=off --no-check-certificate -U mozilla http://cs.rit.edu
cd ..
echo Ending Pull
