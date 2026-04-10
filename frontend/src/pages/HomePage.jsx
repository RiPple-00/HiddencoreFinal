import { useEffect, useMemo, useState } from 'react';
import { getRoomSummary } from '../api/LocationApi';
import RoomComponent from '../components/lemon/RoomComponent';

const WARDS = ['A동', 'B동', 'C동'];
const FLOORS = [1, 2, 3, 4, 5];
const SPECIALS = ['중환자실', '격리실'];

const btnBaseClass = 'rounded-lg px-4 py-2 text-sm font-medium transition';
const btnActiveClass = 'bg-blue-600 text-white';
const btnInactiveClass = 'bg-white text-gray-800 hover:bg-blue-50';

const getStatus = (occupiedBeds) => {
  if (occupiedBeds === 4) return 'FULL';
  if (occupiedBeds <= 1) return 'AVAILABLE';
  return 'WARNING';
};

const buildRoomData = (ward, floor) => {
  const wardSeed = ward.charCodeAt(0) - 65;
  return Array.from({ length: 10 }, (_, idx) => {
    const roomNo = idx + 1;
    const occupiedBeds = (roomNo + floor + wardSeed) % 5;
    let roomType = '일반실';
    if (roomNo === 3) roomType = '중환자실';
    if (roomNo === 9) roomType = '격리실';
    return {
      id: `${ward}-${floor}-${roomNo}`,
      roomNumber: `${ward}${floor}${String(roomNo).padStart(2, '0')}`,
      gender: (roomNo + wardSeed) % 2 === 0 ? '남성' : '여성',
      occupiedBeds,
      totalBeds: 4,
      status: getStatus(occupiedBeds),
      roomType,
      nurses: occupiedBeds === 0 ? ['S'] : ['K', 'L', 'M', 'S'].slice(0, Math.max(1, occupiedBeds)),
    };
  });
};

const isRoomHighlighted = (roomType, selectedSpecial) => {
  if (!selectedSpecial) return roomType === '일반실';
  return roomType === selectedSpecial;
};

function HomePage() {
  const [selectedWard, setSelectedWard] = useState('A');
  const [selectedFloor, setSelectedFloor] = useState(1);
  const [selectedSpecial, setSelectedSpecial] = useState(null);
  const [roomCounts, setRoomCounts] = useState({});

  const roomData = useMemo(
    () => {
      const baseRooms = buildRoomData(selectedWard, selectedFloor);

      return baseRooms.map((room) => {
        const key = room.roomNumber;
        const patientCount = roomCounts[key];
        const occupiedBeds = typeof patientCount === 'number' ? patientCount : room.occupiedBeds;

        return {
          ...room,
          occupiedBeds,
          status: getStatus(occupiedBeds),
        };
      });
    },
    [selectedWard, selectedFloor, roomCounts]
  );

  const topRooms = roomData.slice(0, 5);
  const bottomRooms = roomData.slice(5, 10);

  useEffect(() => {
    const fetchRoomSummary = async () => {
      try {
        const summary = await getRoomSummary({
          building: selectedWard,
          floor: selectedFloor,
        });

        const counts = {};
        summary.forEach((item) => {
          const rawRoom = String(item.room ?? '');
          const roomDigits = rawRoom.replace(/\D/g, '');
          const roomSuffix = roomDigits.length >= 2 ? roomDigits.slice(-2) : rawRoom.padStart(2, '0');
          const roomKey = `${item.building}${item.floor}${roomSuffix}`;
          counts[roomKey] = item.patientCount ?? 0;
        });

        setRoomCounts(counts);
      } catch (error) {
        console.error('병실 환자 수 조회 실패:', error);
        setRoomCounts({});
      }
    };

    fetchRoomSummary();
  }, [selectedWard, selectedFloor]);

  return (
    <section className="space-y-5">
      <div className="rounded-2xl bg-gray-300 p-6">
        <div className="flex flex-wrap gap-10">
          <div>
            <p className="mb-4 text-lg text-blue-600">WARD SELECTION</p>
            <div className="flex gap-3">
              {WARDS.map((ward) => (
                <button
                  key={ward}
                  type="button"
                  onClick={() => setSelectedWard(ward)}
                  className={`${btnBaseClass} ${selectedWard === ward ? btnActiveClass : btnInactiveClass}`}
                >
                  병동{ward}
                </button>
              ))}
            </div>
          </div>

          <div>
            <p className="mb-4 text-lg text-blue-600">FLOOR SELECTION</p>
            <div className="flex gap-2">
              {FLOORS.map((floor) => (
                <button
                  key={floor}
                  type="button"
                  onClick={() => setSelectedFloor(floor)}
                  className={`${btnBaseClass} rounded ${selectedFloor === floor ? btnActiveClass : btnInactiveClass}`}
                >
                  {floor}F
                </button>
              ))}
            </div>
          </div>

          <div>
            <p className="mb-4 text-lg text-blue-600">SPECIAL SELECTION</p>
            <div className="flex gap-2">
              {SPECIALS.map((special) => (
                <button
                  key={special}
                  type="button"
                  onClick={() =>
                    setSelectedSpecial((prev) => (prev === special ? null : special))
                  }
                  className={`${btnBaseClass} ${selectedSpecial === special ? btnActiveClass : btnInactiveClass}`}
                >
                  {special}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="rounded-2xl border border-gray-200 bg-gray-50 p-4 md:p-5">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-5">
          {topRooms.map((room) => (
            <RoomComponent
              key={room.id}
              room={room}
              isDimmed={!isRoomHighlighted(room.roomType, selectedSpecial)}
            />
          ))}
        </div>

        <div className="my-5 flex h-28 items-center justify-center rounded-xl border border-blue-200 bg-gray-200 text-2xl font-bold tracking-widest text-slate-500">
          복도(HALL)
        </div>

        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-5">
          {bottomRooms.map((room) => (
            <RoomComponent
              key={room.id}
              room={room}
              isDimmed={!isRoomHighlighted(room.roomType, selectedSpecial)}
            />
          ))}
        </div>

        <div className="mt-5 flex items-center justify-center gap-3 text-xl font-semibold text-gray-500">
          <button type="button" className="text-blue-600">1</button>
          <button type="button" className="hover:text-gray-800">2</button>
          <button type="button" className="hover:text-gray-800">3</button>
          <button type="button" className="hover:text-gray-800">4</button>
          <button type="button" className="hover:text-gray-800">5</button>
        </div>
      </div>
    </section>
  );
}

export default HomePage;